package com.growthmul.app.lawnmover_fs.service;

import com.growthmul.app.lawnmover_fs.dto.ReviewDto;
import com.growthmul.app.lawnmover_fs.dto.ReviewPageDto;
import com.growthmul.app.lawnmover_fs.dto.ReviewSubmitRequest;
import com.growthmul.app.lawnmover_fs.entity.Company;
import com.growthmul.app.lawnmover_fs.entity.Review;
import com.growthmul.app.lawnmover_fs.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class ReviewService {

    @Autowired private ReviewRepository reviewRepo;
    @Autowired private PublicTenantResolver tenantResolver;

    // ───────────────────────── PUBLIC ─────────────────────────

    // Kept for anything that still wants the full approved list in one
    // shot (e.g. the summary strip's top couple of reviews).
    public List<ReviewDto> getApprovedReviews(String origin) {
        Company company = tenantResolver.resolve(origin);
        return reviewRepo
                .findByCompanyIdAndApprovedTrueOrderBySubmittedAtDesc(company.getId())
                .stream().map(ReviewDto::from).toList();
    }

    // Paginated variant backing the "Show All" reviews view — pulls one
    // page (10 by default) at a time instead of the whole table, so a
    // business with hundreds of reviews can't blow up the response or
    // the DOM on a single request.
    public ReviewPageDto getApprovedReviewsPage(String origin, int page, int size) {
        Company company = tenantResolver.resolve(origin);
        int safePage = Math.max(page, 0);
        int safeSize = Math.min(Math.max(size, 1), 50); // hard ceiling so ?size=99999 can't be used to bypass paging
        Pageable pageable = PageRequest.of(safePage, safeSize);
        Page<Review> result = reviewRepo
                .findByCompanyIdAndApprovedTrueOrderBySubmittedAtDesc(company.getId(), pageable);
        return ReviewPageDto.from(result);
    }

    public void submitReview(String origin, ReviewSubmitRequest req) {
        Company company = tenantResolver.resolve(origin);

        if (req.getReviewerName() == null || req.getReviewerName().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name is required");
        if (req.getText() == null || req.getText().isBlank())
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Review text is required");
        if (req.getStars() < 1 || req.getStars() > 5)
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Stars must be between 1 and 5");

        Review review = new Review();
        review.setReviewerName(req.getReviewerName().trim());
        review.setReviewerCity(req.getReviewerCity() != null ? req.getReviewerCity().trim() : "");
        review.setStars(req.getStars());
        review.setText(req.getText().trim());
        review.setApproved(false); // always starts pending
        review.setCompany(company);
        reviewRepo.save(review);
    }

    // ───────────────────────── ADMIN ─────────────────────────

    public List<ReviewDto> getAllReviews(Long companyId) {
        return reviewRepo.findByCompanyIdOrderBySubmittedAtDesc(companyId)
                .stream().map(ReviewDto::from).toList();
    }

    public void approveReview(Long companyId, Long reviewId) {
        Review review = ownedOrThrow(companyId, reviewId);
        review.setApproved(true);
        reviewRepo.save(review);
    }

    public void deleteReview(Long companyId, Long reviewId) {
        Review review = ownedOrThrow(companyId, reviewId);
        reviewRepo.delete(review);
    }

    private Review ownedOrThrow(Long companyId, Long reviewId) {
        Review review = reviewRepo.findById(reviewId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Review not found"));
        if (!review.getCompany().getId().equals(companyId))
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Not your review");
        return review;
    }
}

package com.growthmul.app.lawnmover_fs.controller;

import com.growthmul.app.lawnmover_fs.dto.ReviewDto;
import com.growthmul.app.lawnmover_fs.dto.ReviewPageDto;
import com.growthmul.app.lawnmover_fs.dto.ReviewSubmitRequest;
import com.growthmul.app.lawnmover_fs.repository.CompanyRepo;
import com.growthmul.app.lawnmover_fs.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired private ReviewService reviewService;
    @Autowired
    private CompanyRepo companyRepo;

    @GetMapping
    public List<ReviewDto> getApproved(@RequestHeader(value = "Origin", required = false) String origin) {
        return reviewService.getApprovedReviews(origin);
    }

    // GET /reviews/page?page=0&size=10
    // -> { content: [...], page, size, totalPages, totalElements, hasNext }
    // Backs the storefront's "Show All" reviews view — one page at a time
    // instead of the whole approved-reviews table in a single response.
    @GetMapping("/page")
    public ReviewPageDto getApprovedPage(
            @RequestHeader(value = "Origin", required = false) String origin,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return reviewService.getApprovedReviewsPage(origin, page, size);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submit(@RequestHeader(value = "Origin", required = false) String origin,
                       @RequestBody ReviewSubmitRequest req) {
        System.out.println("Reviews URL has been hit");
        reviewService.submitReview(origin, req);
    }
}

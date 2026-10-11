package com.growthmul.app.lawnmover_fs.dto;

import org.springframework.data.domain.Page;

import java.util.List;

// Response shape for GET /reviews?page=&size= — the storefront's "Show
// All" view fetches one page at a time (10 reviews/page by default)
// instead of the whole approved-reviews table, so a business with a
// large review history can't pull everything into memory/DOM at once.
public class ReviewPageDto {
    private List<ReviewDto> content;
    private int page;
    private int size;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;

    public static ReviewPageDto from(Page<com.growthmul.app.lawnmover_fs.entity.Review> pageResult) {
        ReviewPageDto dto = new ReviewPageDto();
        dto.content = pageResult.getContent().stream().map(ReviewDto::from).toList();
        dto.page = pageResult.getNumber();
        dto.size = pageResult.getSize();
        dto.totalPages = pageResult.getTotalPages();
        dto.totalElements = pageResult.getTotalElements();
        dto.hasNext = pageResult.hasNext();
        return dto;
    }

    public List<ReviewDto> getContent() { return content; }
    public int getPage() { return page; }
    public int getSize() { return size; }
    public int getTotalPages() { return totalPages; }
    public long getTotalElements() { return totalElements; }
    public boolean isHasNext() { return hasNext; }
}

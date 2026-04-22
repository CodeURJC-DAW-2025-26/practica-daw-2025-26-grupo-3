import { create } from "zustand";
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import type { ReviewPostDTO } from "~/dtos/ReviewPostDTO";
import { createProductReview, deleteReviewById, getReviewByProduct } from "~/services/review-service";

export interface ReviewState {
    reviews: ReviewDTO[],
    getProductReviews: (id: number) => void,
    createReview: (id: number, data: ReviewPostDTO) => void,
    deleteReview: (reviewId: number, productId: number) => void
}

export const useReviewState = create<ReviewState>((set, get) => ({
    reviews: [],
    getProductReviews: async (id: number) => {
        try {
            const reviewList = await getReviewByProduct(id);
            set({ reviews: reviewList });
        }
        catch (err) {
            throw err;
        }
    },
    createReview: async (id: number, data: ReviewPostDTO) => {
        try {
            const newReview = await createProductReview(id, data);
            set({ reviews: [...get().reviews, newReview] });
        } catch (err) {
            throw err;
        }
    },
    deleteReview: async (reviewId: number, productId: number) => {
        try {
            const deletedReview = await deleteReviewById(reviewId, productId);
            set({ reviews: get().reviews.filter(review => review.id !== deletedReview.id && review) });
        }
        catch (err) {
            throw err;
        }
    }

}));

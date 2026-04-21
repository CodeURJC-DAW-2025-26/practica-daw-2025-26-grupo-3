import { create } from "zustand";
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import type { ReviewPostDTO } from "~/dtos/ReviewPostDTO";
import { createProductReview, getReviewByProduct } from "~/services/review-service";

export interface ReviewState {
    reviews: ReviewDTO[],
    getProductReviews: (id: number) => void,
    createReview: (id: number, data: ReviewPostDTO) => void
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
            set({ reviews: [...get().reviews, newReview] })
        } catch (err) {
            throw err;
        }

    }
}));

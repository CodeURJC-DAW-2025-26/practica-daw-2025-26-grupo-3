import { create } from "zustand";
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import { getReviewByProduct } from "~/services/review-service";

export interface ReviewState {
    reviews: ReviewDTO[] | null,
    getProductReviews: (id: number) => void
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
    }
}));

// Import Zustand's 'create' function to instantiate the global store
import { create } from "zustand";

// Import Data Transfer Objects (DTOs) for typing the reviews
import type { ReviewDTO } from "~/dtos/ReviewDTO";
import type { ReviewPostDTO } from "~/dtos/ReviewPostDTO";

// Import the service functions that handle the actual HTTP requests to the REST API
import { createProductReview, deleteReviewById, getReviewByProduct, updateReview } from "~/services/review-service";

// 1. STATE INTERFACE
// Defines the shape of our store: what data it holds and what actions can modify it
export interface ReviewState {
    reviews: ReviewDTO[], // Array containing the current list of reviews

    // Actions for CRUD operations
    getProductReviews: (id: number) => void,
    createReview: (id: number, data: ReviewPostDTO) => void,
    deleteReview: (reviewId: number, productId: number) => void,
    editReview: (reviewId: number, productId: number, newReviewData: ReviewPostDTO) => void
}

// 2. STORE IMPLEMENTATION
export const useReviewState = create<ReviewState>((set, get) => ({
    // Initial state: start with an empty array of reviews
    reviews: [],

    // READ: Fetch all reviews for a specific product
    getProductReviews: async (id: number) => {
        try {
            // Call the backend API to get the reviews for the given product ID
            const reviewList = await getReviewByProduct(id);
            // Update the global state with the fetched list
            set({ reviews: reviewList });
        }
        catch (err) {
            throw err;
        }
    },

    // CREATE: Post a new review for a product
    createReview: async (id: number, data: ReviewPostDTO) => {
        try {
            // Send the new review data to the backend
            await createProductReview(id, data);

            // Fetch the updated list of reviews to ensure all data (like nested user objects) is loaded correctly
            await get().getProductReviews(id);
        } catch (err) {
            throw err;
        }
    },

    // DELETE: Remove a review
    deleteReview: async (reviewId: number, productId: number) => {
        try {
            // Tell the backend to delete the review from the database
            const deletedReview = await deleteReviewById(reviewId, productId);

            // DEFENSE NOTE: We use the .filter() array method to create a new array 
            // that includes all reviews EXCEPT the one that matches the deleted ID.
            // This removes the item from the frontend state.
            set({ reviews: get().reviews.filter(review => review.id !== deletedReview.id && review) });
        }
        catch (err) {
            throw err;
        }
    },

    // UPDATE: Edit an existing review
    editReview: async (reviewId: number, productId: number, newReviewData: ReviewPostDTO) => {
        try {
            // Send the updated data to the backend
            await updateReview(reviewId, productId, newReviewData);

            // Fetch the updated list of reviews to ensure all data (like nested user objects) is loaded correctly
            await get().getProductReviews(productId);
        }
        catch (err) {
            throw err;
        }
    }
}));
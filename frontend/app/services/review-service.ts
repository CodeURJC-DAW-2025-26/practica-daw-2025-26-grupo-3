import type { ReviewPostDTO } from "~/dtos/ReviewPostDTO";

const base_url = "/api/v1/products"

export async function getReviewByProduct(id: number) {
    const url = `${base_url}/${id}/reviews`;
    const response = await fetch(url, {
        credentials: "include",
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}

export async function createProductReview(id: number, reviewData: ReviewPostDTO) {
    const url = `${base_url}/${id}/reviews`;

    const response = await fetch(url, {
        method: "POST",
        credentials: "include",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(reviewData)
    });

    if (!response.ok) {
        const errorData = await response.json();
        throw new Error(errorData.message);
    }

    return await response.json();
}
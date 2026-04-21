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
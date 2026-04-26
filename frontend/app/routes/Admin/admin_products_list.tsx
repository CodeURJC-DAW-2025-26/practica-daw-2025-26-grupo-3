import React from 'react';
import { useLoaderData } from 'react-router';

// Import the reusable ProductList component
import ProductList from '~/components/Product/product_list';

// Import the service function to fetch all products
import { getAllProducts } from '~/services/product-service';
import type { ProductBasicDTO } from '~/dtos/ProductBasicDTO';

// Client loader: Fetches the required data before rendering the component
export async function clientLoader() {
    try {
        // Request the products from the backend API
        const response = await getAllProducts();

        // Safe extraction: Check if the response is already an array.
        // If it's a PageDTO (Spring Boot pagination), extract the 'content' array.
        const productsArray = Array.isArray(response) ? response : (response?.content || []);

        // Return the extracted array wrapped in an object for consistent destructuring
        return { products: productsArray as ProductBasicDTO[] };

    } catch (error) {
        console.error("Error loading products in admin view:", error);
        // Fallback: Return an empty array to prevent the UI from crashing if the fetch fails
        return { products: [] as ProductBasicDTO[] };
    }
}

// Main component for the Admin Published Products view
export default function AdminProductsList() {
    // Destructure 'products' from the loader data, leveraging TypeScript inference
    const { products } = useLoaderData<typeof clientLoader>();

    return (
        // Main wrapper: Flex column layout ensuring it takes full height/width
        <div className="d-flex flex-column h-100 w-100">

            {/* --- HEADER SECTION --- */}
            <section className="py-5 bg-light border-bottom">
                <div className="container">
                    <div className="d-flex justify-content-between align-items-center">
                        <div>
                            {/* Page title kept in Spanish as requested by the UI design */}
                            <h1 className="fw-bold mb-0">Productos Publicados</h1>
                        </div>
                    </div>
                </div>
            </section>

            {/* --- MAIN CONTENT SECTION --- */}
            {/* 'flex-grow-1' expands this section to fill the remaining vertical space */}
            <div className="flex-grow-1 bg-white">
                <ProductList
                    products={products}
                    isOwnerMode={true} // Crucial: Enables Admin actions (View, Edit, Delete)
                    emptyTitle="No se han encontrado productos publicados."
                    emptySubtitle=""
                />
            </div>

        </div>
    );
}
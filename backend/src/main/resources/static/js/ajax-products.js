// Wait for the HTML document to fully load before running the script
document.addEventListener("DOMContentLoaded", () => {

    // we get the elements from the HTML
    const loadMoreBtn = document.getElementById('load-more-btn');
    const productGrid = document.getElementById('featured-products');


    // We start asking for page 2 (pages 0 and 1 are already loaded on screen)
    let currentPage = 2;

    // Listen for the click event on the button "Load more"
    loadMoreBtn.addEventListener('click', async () => {

        // Save the original button text and set it to a loading state
        const originalText = loadMoreBtn.innerHTML;
        loadMoreBtn.innerHTML = 'Cargando...';
        loadMoreBtn.disabled = true; //Disables the button when the products are loading, This is to prevent the user from making requests while they are loading.


        try {
            // Fetch the new products from the backend, we pass the page as a parameter
            const response = await fetch(`/products/more?page=${currentPage}`);
            const html = await response.text();
            const cleanHtml = html.trim(); //we delete the blank spaces

            // If the server returns nothing, we reached the end of the product list 
            if (!cleanHtml) {
                loadMoreBtn.remove(); //we remove the load more button 
                return;
            }

            // Insert the new HTML elements at the end of our product grid
            productGrid.insertAdjacentHTML('beforeend', cleanHtml);

            // Create a temporary container to count how many cards we just received
            const tempContainer = document.createElement('div');
            tempContainer.innerHTML = cleanHtml;
            const loadedProductsCount = tempContainer.querySelectorAll('.product-card').length;

            // If we received less than 4 products, there are no more products left to load
            const isLastPage = tempContainer.querySelector('#is-last-page');

            if (loadedProductsCount < 4 || isLastPage) {
                loadMoreBtn.remove();
            } else {
                // If we got exactly 4, prepare the variables for the next click, we reconfigure the button 
                currentPage++; //we pass to the next page
                loadMoreBtn.innerHTML = originalText; //we restore the original button text 
                loadMoreBtn.disabled = false;
            }

        } catch (error) {
            // If something goes wrong (e.g., network error), log it and restore the button
            console.error('Error fetching more products:', error);
            loadMoreBtn.innerHTML = originalText;
            loadMoreBtn.disabled = false;
        }
    });
});
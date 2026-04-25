import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    // Public routes and User routes (Main layout)
    layout("routes/home.tsx", [
        route("/", "routes/index.tsx"),
        route("product_search", "routes/Products/product_search.tsx"),
        route("product-publish", "routes/Products/product-publish.tsx"),
        route("shopping-cart", "routes/Products/shopping-cart.tsx"),
        route("my_products", "routes/Products/my_products.tsx"),
        route("product_detail/:id", "routes/Products/product_detail.tsx"),
        route("edit_product/:id", "routes/Products/edit_product.tsx"),
        route("edit_review", "routes/Products/review_edit.tsx")
    ]),

    // Non-layout routes (Complete screens)
    route("login", "routes/Users/login.tsx"),
    route("signup", "routes/Users/signup.tsx"),
    route("profile", "routes/Users/profile.tsx"),
    route("profile/edit", "routes/Users/profile_edit.tsx"),

    // Admin routes (Admin layout)
    /* * DEFENSE NOTE: 
     * We use 'layout' to apply our AdminRoute component to all admin pages.
     * The AdminRoute file will act as a guard. If the user is an admin, it renders the <Outlet /> 
     * (which will be the AdminPanel). If not, it redirects them away.
     */
    layout("routes/Admin/AdminRoute.tsx", [
        // base route "/admin" load the AdminPanel component
        route("admin", "routes/Admin/admin_panel.tsx"),

    ])
] satisfies RouteConfig;

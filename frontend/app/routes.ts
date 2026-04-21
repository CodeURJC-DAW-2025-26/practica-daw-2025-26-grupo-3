import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    layout("routes/home.tsx", [
        route("/", "routes/index.tsx"),
        route("product_search", "routes/Products/product_search.tsx"),
        route("product-publish", "routes/Products/product-publish.tsx"),
        route("shopping-cart", "routes/Products/shopping-cart.tsx"),
        route("my_products", "routes/Products/my_products.tsx"),
        route("product_detail/:id", "routes/Products/product_detail.tsx"),
        route("edit_product/:id", "routes/Products/edit_product.tsx")
    ]),
    route("login", "routes/Users/login.tsx"),
    route("signup", "routes/Users/signup.tsx"),
    route("profile", "routes/Users/profile.tsx"),
    route("profile/edit", "routes/Users/profile_edit.tsx")
] satisfies RouteConfig;

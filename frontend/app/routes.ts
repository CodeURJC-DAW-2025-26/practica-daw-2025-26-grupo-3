import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    layout("routes/home.tsx", [
        index("routes/index.tsx"),
        route("product_search", "routes/product_search.tsx"),
        route("product-publish", "routes/product-publish.tsx"),
        route("shopping-cart", "routes/shopping-cart.tsx"),
        route("my_products", "routes/my_products.tsx")
    ]),
    route("login", "routes/login.tsx"),
    route("signup", "routes/signup.tsx"),
    route("profile", "routes/profile.tsx"),
    route("profile/edit", "routes/profile_edit.tsx")
] satisfies RouteConfig;

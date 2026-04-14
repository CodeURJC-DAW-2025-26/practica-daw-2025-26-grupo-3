import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    layout("routes/home.tsx", [
        route("/", "routes/index.tsx", [
            index("routes/Products/product_list.tsx")
        ]),
        route("product_search", "routes/Products/product_search.tsx"),
        route("product-publish", "routes/Products/product-publish.tsx"),
        route("shopping-cart", "routes/Products/shopping-cart.tsx"),
        route("my_products", "routes/Products/my_products.tsx"),
        route("product_detail/:id", "routes/Products/product_detail.tsx")
    ]),
    route("login", "routes/login.tsx"),
    route("signup", "routes/signup.tsx"),
    route("profile", "routes/profile.tsx"),
    route("profile/edit", "routes/profile_edit.tsx")
] satisfies RouteConfig;

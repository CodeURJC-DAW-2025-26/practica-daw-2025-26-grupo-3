import { type RouteConfig, index, layout, route } from "@react-router/dev/routes";

export default [
    layout("routes/home.tsx", [
        index("routes/index.tsx"),
    ]),
    route("login", "routes/login.tsx"),
    route("signup", "routes/signup.tsx"),
    route("profile", "routes/profile.tsx"),
    route("profile/edit", "routes/profile_edit.tsx")
] satisfies RouteConfig;

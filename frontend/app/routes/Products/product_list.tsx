import { Link, useNavigate } from "react-router";
import type { Route } from "./+types/product_list";
import { getProducts } from "~/services/product-service";
import type { ProductBasicDTO } from "~/dtos/ProductBasicDTO";
import { Card, Container, Row, Col, Button } from "react-bootstrap";
import { useUserState } from "~/stores/user-store";


export async function clientLoader({ }: Route.ClientLoaderArgs) {
    return await getProducts();
}

/*
export default function productList({ loaderData }: Route.ComponentProps) {
    const products = loaderData;
    let { user } = useUserState();

    return (

        
    );
}
    */
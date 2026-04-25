import { Link, useLoaderData, useNavigate } from "react-router";
import { publishProduct, uploadProductImage } from "~/services/product-service";
import ProductForm from "~/components/Product/product_form";
import { useState } from "react";

export async function clientLoader() {
    let isAdmin = false;
    
    const userString = localStorage.getItem('user'); 
    if (userString) {
        const user = JSON.parse(userString);

        if (user.roles && user.roles.includes('ADMIN')) {
            isAdmin = true;
        }
    }
    return { isAdmin };
}

export default function ProductPublish() {
    const navigate = useNavigate();
    const { isAdmin } = useLoaderData<typeof clientLoader>();

    const handleCreateAction = async (prevState: any, formData: FormData) => {

        try {
            const ProductData = {
                productName: formData.get("productName") as string,
                description: formData.get("description") as string,
                price: Number(formData.get("price")),
                state: isAdmin ? Number(formData.get("state")) : 2
            };
            const newProduct = await publishProduct(ProductData);

            const newImages = formData.getAll("productimages") as File[];
            for (const file of newImages) {

                if (file.size > 0) {
                    await uploadProductImage(newProduct.id, file);
                }
            }
            navigate('/product_detail/' + newProduct.id);
            return null;
        } catch (error) {
            console.error("Error al publicar el producto:", error);
            return { error: "Hubo un problema de conexión al publicar el producto." };
        }

    }
    return (<ProductForm isEditing={false} 
        isAdmin={isAdmin} 
        actionFunction={handleCreateAction} 
        onCancel={() => navigate('/')} />);
}
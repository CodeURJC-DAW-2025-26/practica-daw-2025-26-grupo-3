import { useParams, useNavigate, useLoaderData, redirect } from "react-router"
import { useState } from "react";
import ProductForm, { type ProductData } from "~/components/Product/product_form";
import { deleteProductImage, getBasicProduct, updateProduct, uploadProductImage } from "~/services/product-service";
import { Container } from "react-bootstrap";
import { Spinner } from "~/components/spinner";
import { requireUserLoader, useUserState } from "~/stores/user-store";

export async function clientLoader({ params }: any) {
    const { id } = params; // We obtain the ID from the URL

    const currentUser = await requireUserLoader();

    if (!currentUser) {
        return redirect("/login"); 
    }

    try {
        const data = await getBasicProduct(Number(id));

        let stateName = "Desconocido";
        switch (data.state) {
            case 0: stateName = "Nuevo"; break;
            case 1: stateName = "Reacondicionado"; break;
            case 2: stateName = "Segunda Mano"; break;
        }

        const productData: ProductData = {
            id: data.id,
            productName: data.productName,
            price: data.price,
            state: data.state.toString(),
            StateName: stateName,
            description: data.description,
            images: data.images?.map((img: any) => ({ id: img.id })) || []
        };

        return { productData, currentUser };

    } catch (error) {
        console.error("Error cargando el producto en el loader", error);
        return { productData: null, currentUser };
    }
}

export default function EditProduct() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const { productData } = useLoaderData<typeof clientLoader>();

    const { currentUser } = useUserState();


    const handleEditAction = async (prevState: any, formData: FormData) => {
        setLoading(true);
        try {

            // we call to the api to update the product texts
            const productData = {
                productName: formData.get("productName") as string,
                description: formData.get("description") as string,
                price: Number(formData.get("price")),
                state: Number(formData.get("state"))
            };

            await updateProduct(Number(id), productData);

            // We remove the images that the user has marked for deletion.
            const imagesToRemove = formData.getAll("removeImages");
            for (const imageId of imagesToRemove) {
                await deleteProductImage(Number(id), Number(imageId));
            }

            //We upload the new images
            const newImages = formData.getAll("productimages") as File[];
            for (const file of newImages) {
                // If the file input is empty, it may return a File with size 0, we ignore it.
                if (file.size > 0) {
                    await uploadProductImage(Number(id), file);
                }
            }

            // When the product is updated, we navigate to the product detail page to see the changes.
            navigate('/product_detail/' + id);

            return null;
        } catch (error) {
            console.error("Error al intentar actualizar el producto", error);
            setLoading(false);
            return { error: "Hubo un problema de conexión al guardar el producto." };
        }
    };

    if (loading) {
        return (
            <Container className="my-5 d-flex flex-column justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <Spinner />
                <p className="text-muted mt-3 fs-5">Cargando producto...</p>
            </Container>
        );
    }

    if (!productData) {
        return <Container className="text-center my-5">Error: Producto no encontrado.</Container>;
    }

    return (
        <ProductForm
            initialData={productData}
            isEditing={true}
            actionFunction={handleEditAction}
            onCancel={() => navigate('/product_detail/' + id)}
        />
    );

}
import { useParams, useNavigate, useLoaderData, redirect } from "react-router"
import ProductForm, { type ProductData } from "~/components/Product/product_form";
import { deleteProductImage, getBasicProduct, updateProduct, uploadProductImage } from "~/services/product-service";
import { Container } from "react-bootstrap";
import { requireUserLoader } from "~/stores/user-store";
import { ErrorCard } from "~/components/error-card";

export async function clientLoader({ params }: { params: { id: string } }) {
    const { id } = params; // We obtain the ID from the URL
    const currentUser = await requireUserLoader();

    if (!currentUser) {
        return { productData: null, currentUser: null, error: "Debes iniciar sesión para editar un producto." };
    }
    const numericId = Number(id);

    if (Number.isNaN(numericId)) {
        return { productData: null, currentUser, error: "ID de producto inválido." };
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

        return { productData, currentUser, error: null };

    } catch (error) {
        console.error("Error cargando el producto en el loader", error);
        return { productData: null, currentUser, error: "No se pudo cargar el producto para editar." };
    }
}

export default function EditProduct() {
    const { id } = useParams();
    const navigate = useNavigate();
    const { productData, currentUser, error } = useLoaderData<typeof clientLoader>();


    const handleEditAction = async (prevState: any, formData: FormData) => {

        try {

            const imagesToRemove = formData.getAll("removeImages");
            const newImages = formData.getAll("productimages") as File[];

            const validNewImagesCount = newImages.filter(file => file.size > 0).length;
            const initialImageCount = productData?.images?.length || 0;
            const finalImageCount = initialImageCount - imagesToRemove.length + validNewImagesCount;

            // If there aren´any images we show an error
            if (finalImageCount < 1) {
                return { error: "El producto debe tener al menos una imagen. Sube una nueva o desmarca alguna de las actuales." };
            }

            const updateData = {
                productName: formData.get("productName") as string,
                description: formData.get("description") as string,
                price: Number(formData.get("price")),
                state: Number(formData.get("state"))
            };

            //We update the data of the product
            await updateProduct(Number(id), updateData);

            // we delete the deleted images
            for (const imageId of imagesToRemove) {
                await deleteProductImage(Number(id), Number(imageId));
            }

            // We upload the new images
            for (const file of newImages) {
                if (file.size > 0) {
                    await uploadProductImage(Number(id), file);
                }
            }

            navigate('/product_detail/' + id);
            return null;
        } catch (error) {
            const errorMessage = error instanceof Error
                ? (error.message.split(":")[1]?.trim() || error.message)
                : "Hubo un problema de conexión al guardar el producto.";
            return { error: errorMessage };
        }
    };
    if (error && !currentUser) {
        return (
            <Container className="my-5 d-flex justify-content-center align-items-center" style={{ minHeight: "60vh" }}>
                <ErrorCard message={error} />
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
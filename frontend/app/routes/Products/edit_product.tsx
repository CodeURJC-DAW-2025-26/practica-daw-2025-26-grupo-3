import { useParams, useNavigate } from "react-router"
import { useEffect, useState } from "react";
import ProductForm, { type ProductData } from "~/components/Product/product_form";
import { deleteProductImage, getBasicProduct, updateProduct, uploadProductImage } from "~/services/product-service";


export default function EditProduct() {
    const { id } = useParams();
    const navigate = useNavigate();
    const [productData, setProductData] = useState<ProductData | null>(null);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        if (id) {
            getBasicProduct(Number(id))
                .then(data => {
                    let stateName = "Desconocido";
                    switch (data.state) {
                        case 0: stateName = "Nuevo"; break;
                        case 1: stateName = "Reacondicionado"; break;
                        case 2: stateName = "Segunda Mano"; break;
                    }

                    setProductData({
                        id: data.id,
                        productName: data.productName,
                        price: data.price,
                        state: data.state.toString(),
                        StateName: stateName,
                        description: data.description,
                        images: data.images?.map(img => ({ id: img.id })) || []
                    });
                    setLoading(false);
                })
                .catch(err => {
                    console.error("Error cargando el producto", err);
                    setLoading(false);
                });
        }
    }, [id]);

    const handleEditAction = async (prevState: any, formData: FormData) => {
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

            return { error: "Hubo un problema de conexión al guardar el producto." };
        }
    };

    if (loading) {
        return <div className="text-center my-5">Cargando producto...</div>;
    }

    if (!productData) {
        return <div className="text-center my-5">Error: Producto no encontrado.</div>;
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
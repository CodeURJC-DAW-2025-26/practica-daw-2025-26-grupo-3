
import React, { useActionState, useState } from 'react';
import { Container, Row, Col, Card, Form, Button, ButtonGroup, Spinner } from 'react-bootstrap';
import { ErrorCard } from '~/components/error-card';

export interface ProductData {
    id: number;
    productName: string;
    price: number;
    state: string;
    StateName: string;
    description: string;
    images?: Array<{ id: number }>;
}

interface ProductFormProps {
    initialData?: ProductData;
    isEditing?: boolean; // true for edition, false for publishing
    isAdmin?:boolean;
    actionFunction: (prevState: any, formData: FormData) => Promise<any>;
    onCancel: () => void;
}

export default function ProductForm({ initialData, isEditing = false,isAdmin = false, actionFunction, onCancel }: ProductFormProps) {

    const [imagesToRemove, setImagesToRemove] = useState<Set<number>>(new Set()); //Image can be removed or not

    const [state, formAction, isPending] = useActionState(actionFunction, null);

    //If the user clicks on the checkbox to remove an image, we toggle its presence in the imagesToRemove set
    const handleToggleRemoveImage = (id: number) => {
        setImagesToRemove(prev => {
            const newSet = new Set(prev);
            if (newSet.has(id)) newSet.delete(id);
            else newSet.add(id);
            return newSet;
        });
    };

    return (
        <Container className="my-5">
            <Row className="justify-content-center">
                <Col md={8}>
                    <Card className="border-0 shadow-sm p-4">
                        <div className="d-flex justify-content-between align-items-start mb-4">
                            <div>
                                <h3 className="fw-bold mb-4">{isEditing ? 'Editar producto' : 'Publicar producto'}</h3>
                            </div>
                        </div>

                        <p className="text-muted small">
                            {isEditing ? 'Completa los datos para actualizar tu artículo.' : 'Completa los datos para poner tu artículo a la venta.'}
                        </p>
                        {state?.error && (
                            <ErrorCard message={state.error} className="mb-3" />
                        )}
                        <Form action={formAction}>
                            <Form.Group className="mb-3" controlId="productName">
                                <Form.Label className="fw-bold">Nombre del Producto</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="productName"
                                    placeholder="Ej. MacBook Pro 2021"
                                    defaultValue={initialData?.productName || ''}
                                    required
                                    minLength={3}
                                    maxLength={100}
                                />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="priceInput">
                                <Form.Label className="fw-bold">Precio (€)</Form.Label>
                                <Form.Control
                                    type="number"
                                    name="price"
                                    placeholder="0.00"
                                    defaultValue={initialData?.price || ''}
                                    required
                                    min="1"
                                    step="0.01"
                                />
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="stateInput">
                                <Form.Label className="fw-bold">Estado del artículo</Form.Label>
                                {isAdmin && !isEditing ? (
                                    // If the user is an admin and is publishing we show the select
                                    <Form.Select name="state" required defaultValue="">
                                        <option value="" disabled>Selecciona el estado...</option>
                                        <option value="0">Nuevo</option>
                                        <option value="1">Reacondicionado</option>
                                        <option value="2">Segunda mano</option>
                                    </Form.Select>
                                ) : (
                                    // If the user is not an admin or it´s editing, we show only the text
                                    <div>
                                        {/*If the user is editing we show the value, if he is publishing we force secondHand state */}
                                        <p className="form-control bg-light mb-0">
                                            {isEditing ? (initialData?.StateName || 'N/A') : 'Segunda mano'}
                                        </p>
                                        <Form.Control 
                                            type="hidden" 
                                            name="state" 
                                            defaultValue={isEditing ? (initialData?.state || '') : '2'} 
                                        />
                                    </div>
                                )}
                            </Form.Group>

                            <Form.Group className="mb-3" controlId="description">
                                <Form.Label className="fw-bold">Descripción</Form.Label>
                                <Form.Control
                                    as="textarea"
                                    rows={4}
                                    placeholder="Describe brevemente el estado y características de tu producto"
                                    name="description"
                                    defaultValue={initialData?.description || ''}
                                    required
                                    minLength={15}
                                    maxLength={1000}
                                />
                            </Form.Group>

                            {isEditing && (
                                <Form.Group className="mb-3">
                                    <Form.Label className="fw-bold">Imágenes actuales</Form.Label>
                                    <div className="d-flex gap-2 mb-3 overflow-auto p-2 bg-white border rounded">
                                        {initialData?.images && initialData.images.length > 0 ? (
                                            initialData.images.map(img => (
                                                <div key={img.id} className="d-flex flex-column align-items-center justify-content-center position-relative">
                                                    <img
                                                        src={`/api/v1/images/${img.id}/media`}
                                                        className="img-thumbnail shadow-sm mb-2"
                                                        style={{ width: '120px', height: '120px', objectFit: 'cover' }}
                                                        alt="Imagen del producto"
                                                        onError={(e) => { e.currentTarget.src = "https://placehold.co/120x120?text=Error" }}
                                                    />
                                                    <input
                                                        type="checkbox"
                                                        className="btn-check"
                                                        name="removeImages"
                                                        id={`remove_${img.id}`}
                                                        value={img.id}
                                                        autoComplete="off"
                                                        checked={imagesToRemove.has(img.id)}
                                                        onChange={() => handleToggleRemoveImage(img.id)}
                                                    />
                                                    <label className="btn btn-outline-danger btn-sm w-100" htmlFor={`remove_${img.id}`}>
                                                        <i className="bi bi-trash"></i> Eliminar
                                                    </label>
                                                </div>
                                            ))
                                        ) : (
                                            <p className="text-muted small m-2 mb-0 fst-italic">Este producto no tiene imágenes actualmente.</p>
                                        )}
                                    </div>
                                </Form.Group>
                            )}

                            <Form.Group className="mb-4">
                                <Form.Label className="fw-bold">Añadir {isEditing ? 'nuevas imágenes' : 'imágenes'}</Form.Label>
                                <Form.Control type="file" name="productimages" accept="image/*" multiple />
                                {isEditing && (
                                    <Form.Text className="text-muted">
                                        Las imágenes que subas aquí se <strong>sumarán</strong> a las que no hayas marcado para eliminar arriba.
                                    </Form.Text>
                                )}
                            </Form.Group>

                            <div className="mt-4">
                                <Button type="submit" variant="primary" disabled={isPending} className="w-100 py-2 fw-bold d-flex align-items-center justify-content-center">
                                    {isPending ? (
                                        <>
                                            <Spinner animation="border" size="sm" role="status" aria-hidden="true" className="me-2" />
                                            Guardando...
                                        </>
                                    ) : (
                                        isEditing ? 'Finalizar edición' : 'Crear producto'
                                    )}
                                </Button>
                            </div>
                        </Form>

                        <div className="mt-4 text-center">
                            <Button variant="danger" className="py-2 fw-bold shadow-sm" onClick={onCancel} title="Cancelar">
                                Cancelar
                            </Button>
                        </div>
                    </Card>
                </Col>
            </Row>
        </Container>
    );
}
//imports 
import React from 'react';
import { Row, Col, Card } from 'react-bootstrap';
// Import useLoaderData for modern React Router data fetching
import { useLoaderData } from 'react-router';

import ProductTypesChart from "~/components/Admin/product_types_chart";
import TopProductChart from "~/components/Admin/top_product_chart";

//import services and DTOs
import { getKPIs, getProductTypes, getTopProduct } from '~/services/admin-service';
import type { KpiDTO } from '~/dtos/KpiDTO';
import type { DataGraphicDTO } from '~/dtos/DataGraphicDTO';
import { requireUserLoader } from "../auth-loaders";

//data loader
export async function clientLoader() {
    try {
        const currentUser = await requireUserLoader();
        if (!currentUser || !currentUser.roles.includes("ADMIN")) {
            return null; // Return null to prevent fetching and throwing errors. AdminRoute will handle the UI.
        }

        const [kpis, topProducts, productTypes] = await Promise.all([
            getKPIs(),
            getTopProduct(),
            getProductTypes()
        ]);

        return {
            kpis,
            topProducts,
            productTypes
        };

    } catch (error) {
        console.error("Error fetching dashboard data:", error);
        // Throwing the error triggers React Router's ErrorBoundary (the "Oops!" screen)
        throw error;
    }
}

//interface 
interface DashboardData {
    kpis: KpiDTO,
    topProducts: DataGraphicDTO,
    productTypes: DataGraphicDTO
}

//function
export default function AdminPanel() {
    //get the data 
    const { kpis, topProducts, productTypes } = useLoaderData() as DashboardData;

    //render TSX
    return (
        <>
            {/* Dashboard Header */}
            <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                <h2 className="h3 fw-bold text-dark">Panel de Control</h2>
            </div>

            {/* KPI CARDS (Stats) */}
            <Row className="g-4 mb-4">
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3 h-100">
                        <h6 className="fw-bold text-muted small text-uppercase">Ventas Totales</h6>
                        <p className="display-6 fw-bold text-primary mb-0">€{kpis.totalAmountMoney.toFixed(2)}</p>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3 h-100">
                        <h6 className="fw-bold text-muted small text-uppercase">Usuarios Registrados</h6>
                        <p className="display-6 fw-bold text-success mb-0">{kpis.totalUsers}</p>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3 h-100">
                        <h6 className="fw-bold text-muted small text-uppercase">Pedidos Pendientes</h6>
                        <p className="display-6 fw-bold text-warning mb-0">{kpis.pendingOrders}</p>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3 h-100">
                        <h6 className="fw-bold text-muted small text-uppercase">Productos Publicados</h6>
                        <p className="display-6 fw-bold text-info mb-0">{kpis.totalProducts}</p>
                    </Card>
                </Col>
            </Row>

            {/* CHARTS SECTION */}
            <Row className="g-4">
                <Col md={6} lg={6}>
                    <Card className="shadow-sm border-0 p-4 h-100">
                        <h6 className="fw-bold mb-3 text-dark">Top Productos</h6>
                        <div>
                            <TopProductChart chartData={topProducts} />
                        </div>
                    </Card>
                </Col>
                <Col md={6} lg={6}>
                    <Card className="shadow-sm border-0 p-4 h-100">
                        <h6 className="fw-bold mb-3 text-dark">Distribución de Productos por Tipo</h6>
                        <div>
                            <ProductTypesChart chartData={productTypes} />
                        </div>
                    </Card>
                </Col>
            </Row>
        </>
    );
}
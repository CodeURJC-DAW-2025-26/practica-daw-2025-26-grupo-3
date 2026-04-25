import React, { useState, useEffect } from 'react';

// Import required components from react-bootstrap
import { Container, Row, Col, Card } from 'react-bootstrap';

// Import Chart.js requirements for React
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';
import { Bar } from 'react-chartjs-2';

// Register ChartJS components so they can be used within the React wrapper
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// We use 'export default function' to ensure compatibility with React Router's file-based routing.
export default function AdminPanel() {
    // 1. STATE DEFINITION
    // Define the state for the main dashboard KPIs (Key Performance Indicators)
    const [stats, setStats] = useState({
        totalAmountMoney: 0,
        totalUsers: 0,
        pendingOrders: 0,
        totalProducts: 0
    });

    /* * DEFENSE NOTE: 
     * We must explicitly define the generic types for these states in TypeScript.
     * If we just use useState([]), TypeScript infers the type as 'never[]'.
     * By specifying <{ names: string[], sales: number[] }> and <number[]>, 
     * we tell TypeScript exactly what kind of data to expect.
     */
    const [topProducts, setTopProducts] = useState<{ names: string[], sales: number[] }>({ names: [], sales: [] });
    const [productTypes, setProductTypes] = useState<number[]>([]);

    // 2. INITIALIZATION PHASE
    /* * DEFENSE NOTE: 
     * The useEffect hook with an empty dependency array [] runs exactly once when the component mounts.
     * This is the perfect place to fetch the dashboard data from the Spring Boot REST API.
     */
    useEffect(() => {
        // TODO: Replace these hardcoded values with actual fetch/axios calls to the API

        // Mock data for the top stat cards
        setStats({
            totalAmountMoney: 12500,
            totalUsers: 342,
            pendingOrders: 15,
            totalProducts: 120
        });

        // Mock data for the charts
        setTopProducts({
            names: ['Product A', 'Product B', 'Product C', 'Product D', 'Product E'],
            sales: [25, 20, 15, 10, 5] // <-- Aquí están los números que faltaban
        });

        // Mock data representing 'Nuevos', 'Reacondicionados', 'Segunda Mano'
        setProductTypes([60, 35, 25]); // Nuevos, Reacondicionados, Segunda Mano
    }, []);

    // 3. CHART CONFIGURATIONS
    // Configuration object for the "Top Products" Bar Chart
    const productsChartData = {
        labels: topProducts.names,
        datasets: [
            {
                label: 'Unidades Vendidas',
                data: topProducts.sales,
                backgroundColor: 'rgba(13, 110, 253, 0.7)', // Bootstrap primary blue
                borderColor: 'rgba(13, 110, 253, 1)',
                borderWidth: 1,
                borderRadius: 4
            }
        ]
    };

    const productsChartOptions = {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true,
                ticks: { stepSize: 1 } // Ensure no decimal numbers on the Y axis
            }
        }
    };

    // Configuration object for the "Product Types" Horizontal Bar Chart
    const typesChartData = {
        labels: ['Nuevos', 'Reacondicionados', 'Segunda Mano'],
        datasets: [
            {
                label: 'Cantidad de Productos',
                data: productTypes,
                backgroundColor: [
                    'rgba(25, 135, 84, 0.8)',  // Success Green
                    'rgba(255, 193, 7, 0.8)',  // Warning Yellow
                    'rgba(220, 53, 69, 0.8)'   // Danger Red
                ],
                borderColor: [
                    'rgba(25, 135, 84, 1)',
                    'rgba(255, 193, 7, 1)',
                    'rgba(220, 53, 69, 1)'
                ],
                borderWidth: 2,
                borderRadius: 4
            }
        ]
    };

    const typesChartOptions = {
        responsive: true,
        indexAxis: 'y' as const, // This specific property makes the bar chart horizontal
        scales: {
            x: {
                beginAtZero: true,
                ticks: { stepSize: 1 }
            }
        },
        plugins: {
            legend: {
                display: true,
                position: 'top' as const
            }
        }
    };

    // 4. RENDER JSX
    // DEFENSE NOTE: We removed the Container, Row, and Sidebar from here
    // because they are now globally handled by AdminRoute.tsx.
    // This component now ONLY cares about rendering the dashboard content.
    return (
        <>
            {/* Dashboard Header */}
            <div className="d-flex justify-content-between align-items-center mb-4 border-bottom pb-2">
                <h2 className="h3 fw-bold text-dark">Panel de Control</h2>
            </div>

            {/* KPI CARDS (Stats) */}
            <Row className="g-4 mb-4">
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3">
                        <h6 className="fw-bold text-muted small text-uppercase">Ventas Totales</h6>
                        <p className="display-6 fw-bold text-primary mb-0">€{stats.totalAmountMoney}</p>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3">
                        <h6 className="fw-bold text-muted small text-uppercase">Usuarios Registrados</h6>
                        <p className="display-6 fw-bold text-success mb-0">{stats.totalUsers}</p>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3">
                        <h6 className="fw-bold text-muted small text-uppercase">Pedidos Pendientes</h6>
                        <p className="display-6 fw-bold text-warning mb-0">{stats.pendingOrders}</p>
                    </Card>
                </Col>
                <Col md={3}>
                    <Card className="shadow-sm border-0 text-center p-3">
                        <h6 className="fw-bold text-muted small text-uppercase">Productos Publicados</h6>
                        <p className="display-6 fw-bold text-info mb-0">{stats.totalProducts}</p>
                    </Card>
                </Col>
            </Row>

            {/* CHARTS SECTION */}
            <Row className="g-4">
                <Col md={6} lg={6}>
                    <Card className="shadow-sm border-0 p-4 h-100">
                        <h6 className="fw-bold mb-3 text-dark">Top Productos</h6>
                        <div>
                            {/* react-chartjs-2 automatically renders the underlying canvas */}
                            <Bar data={productsChartData} options={productsChartOptions} />
                        </div>
                    </Card>
                </Col>
                <Col md={6} lg={6}>
                    <Card className="shadow-sm border-0 p-4 h-100">
                        <h6 className="fw-bold mb-3 text-dark">Distribución de Productos por Tipo</h6>
                        <div>
                            <Bar data={typesChartData} options={typesChartOptions} />
                        </div>
                    </Card>
                </Col>
            </Row>
        </>
    );
}
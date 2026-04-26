// imports 
import React from 'react';
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

import type { DataGraphicDTO } from '~/dtos/DataGraphicDTO';

//register components of Chart.js
ChartJS.register(CategoryScale, LinearScale, BarElement, Title, Tooltip, Legend);

// interface
interface ProductTypesChartProps {
    chartData: DataGraphicDTO;
}

// function
export default function ProductTypesChart({ chartData }: ProductTypesChartProps) {
    // config data
    const typesChartData = {
        labels: chartData.labels,
        datasets: [
            {
                label: 'Cantidad de Productos',
                data: chartData.data,
                backgroundColor: [
                    'rgba(25, 135, 84, 0.8)',
                    'rgba(255, 193, 7, 0.8)',
                    'rgba(220, 53, 69, 0.8)'
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

    // visual options
    const typesChartOptions = {
        responsive: true,
        indexAxis: 'y' as const,
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

    return <Bar data={typesChartData} options={typesChartOptions} />;
}
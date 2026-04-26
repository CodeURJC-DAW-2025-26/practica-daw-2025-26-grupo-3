//imports 
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

//interface
interface TopProductChart {
    chartData: DataGraphicDTO;
}

//function
export default function TopProductChart({ chartData }: TopProductChart) {
    // config data injection
    const productsChartData = {
        labels: chartData.labels,
        datasets: [
            {
                label: 'Unidades Vendidas',
                data: chartData.data,
                backgroundColor: 'rgba(13, 110, 253, 0.7)',
                borderColor: 'rgba(13, 110, 253, 1)',
                borderWidth: 1,
                borderRadius: 4
            }
        ]
    };

    // graphic visual options
    const productsChartOptions = {
        responsive: true,
        scales: {
            y: {
                beginAtZero: true,
                ticks: { stepSize: 1 }
            }
        }
    };

    return <Bar data={productsChartData} options={productsChartOptions} />;
}
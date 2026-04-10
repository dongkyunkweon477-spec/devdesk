/* =========================================
   DevDesk 관리자 대시보드 차트 그리기
========================================= */

document.addEventListener('DOMContentLoaded', function () {

    // JSP에서 세팅해준 전역 변수 가져오기
    const memberTrendData = chartData_memberTrend;
    const jobDistributionData = chartData_jobDistribution;

    // 1. 가입자 트렌드 차트 (선 차트)
    const ctxLine = document.getElementById('memberTrendChart').getContext('2d');
    new Chart(ctxLine, {
        type: 'line',
        data: {
            labels: ['7일 전', '6일 전', '5일 전', '4일 전', '3일 전', '2일 전', '어제'],
            datasets: [{
                label: '신규 가입자',
                data: memberTrendData,
                borderColor: '#7c3aed',
                backgroundColor: 'rgba(124, 58, 237, 0.1)',
                borderWidth: 3,
                fill: true,
                tension: 0.4
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {display: false}
            },
            scales: {
                y: {
                    beginAtZero: true,
                    grid: {color: '#e2e8f0'},
                    ticks: {color: '#64748b'}
                },
                x: {
                    grid: {display: false},
                    ticks: {color: '#64748b'}
                }
            }
        }
    });

    // 2. 직무 카테고리 분포 차트 (도넛 차트)
    const ctxDoughnut = document.getElementById('jobDistributionChart').getContext('2d');
    new Chart(ctxDoughnut, {
        type: 'doughnut',
        data: {
            labels: ['백엔드', '프론트엔드', 'AI', '기타'],
            datasets: [{
                data: jobDistributionData,
                backgroundColor: [
                    '#7c3aed',
                    '#a78bfa',
                    '#ddd6fe',
                    '#e2e8f0'
                ],
                borderWidth: 0
            }]
        },
        options: {
            responsive: true,
            maintainAspectRatio: false,
            plugins: {
                legend: {
                    position: 'right',
                    labels: {
                        color: '#475569',
                        font: {size: 14}
                    }
                }
            }
        }
    });

});
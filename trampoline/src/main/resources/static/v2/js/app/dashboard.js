var pieChart;
var lineChart;

function updateMemoryCharts(){
        $.ajax({
    	    url : "/dashboard/memory",
    	    type: "GET",
    	    success: function(dataRetrieved, textStatus, jqXHR) {
    	        updatePieChart(dataRetrieved);
    	        updateLineChart(dataRetrieved);

    	    }
    	});
}

function updatePieChart(dataRetrieved){
    pieChart.config.data.datasets = [{
                                      data: [parseFloat(dataRetrieved[dataRetrieved.length-1].totalMemoryMB) - parseFloat(dataRetrieved[dataRetrieved.length-1].freeMemoryMB),
                                            parseFloat(dataRetrieved[dataRetrieved.length-1].totalMemoryMB),
                                            parseFloat(dataRetrieved[dataRetrieved.length-1].freeMemoryMB)],
                                      backgroundColor: ['rgba(255, 99, 132, 0.8)', 'rgba(54, 162, 235, 0.8)','rgba(255, 206, 86, 0.8)']
                                    }];
    pieChart.update();
}

function updateLineChart(dataRetrieved){
    dataMemoryFree = [];
     dates = [];
    $.each(dataRetrieved, function (index, value) {
        dates.push(value.date);
        dataMemoryFree.push(parseFloat(dataRetrieved[dataRetrieved.length-1].freeMemoryMB));
    });
    lineChart.config.data = {
                                  labels: dates,
                                  datasets: [{
                                      label: "Free Memory [MB]",
                                      backgroundColor: 'rgba(255, 99, 132, 0.2)',
                                      borderColor: 'rgba(255, 99, 132, 1)',
                                      data: dataMemoryFree,
                                  }]
                              }

  lineChart.update();
}


setInterval(updateMemoryCharts, 20000);


$( document ).ready(function() {
	var ctx = document.getElementById('memory-pie').getContext('2d');
	pieChart = new Chart(ctx,{
        type: 'doughnut',
        data: {datasets: [{
                      data: [],
                      backgroundColor: ['rgba(255, 99, 132, 0.8)',
                                                              'rgba(54, 162, 235, 0.8)',
                                                              'rgba(255, 206, 86, 0.8)']
                  }],
               labels: ['Used [MB]', 'Total [MB]','Free [MB]']},
        options: {}
    });

    var ctx2 = document.getElementById('memory-line').getContext('2d');
    	lineChart = new Chart(ctx2, {
                               type: 'line',
                               data: {
                                   labels: [],
                                   datasets: [{
                                       label: "Memory Free MB",
                                       backgroundColor: 'rgba(255, 99, 132, 0.2)',
                                       borderColor: 'rgba(255, 99, 132, 1)',
                                       data: [],
                                   }]
                               },
                               options: {}
                           });


    updateMemoryCharts()

	/*var ctx = document.getElementById('metrics-chart').getContext('2d');
        metricsCharts = new Chart(ctx, {
            type: 'line',
            data: {
                labels: [],
                datasets: [{
                    label: "Memory Free KB",
                    backgroundColor: 'rgba(255, 99, 132, 0.2)',
                    borderColor: 'rgba(255, 99, 132, 1)',
                    data: [],
                },
                {
                    label: "Heap Used KB",
                    backgroundColor: 'rgba(54, 162, 235, 0.2)',
                    borderColor: 'rgba(54, 162, 235, 1)',
                    data: [],
                }]
            },
            options: {}
        });*/
});



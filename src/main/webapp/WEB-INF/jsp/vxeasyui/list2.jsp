<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="UTF-8">
    <title>EasyUI for Vue</title>
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/default/easyui.css">
    <link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/icon.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/color.css">
	<link rel="stylesheet" type="text/css" href="http://www.jeasyui.com/easyui/themes/vue.css">
    <script src="http://www.jeasyui.com/node_modules/vx-easyui/vue.min.js"></script>
    <script src="http://www.jeasyui.com/node_modules/vx-easyui/babel-polyfill.js"></script>
    <script src="http://www.jeasyui.com/node_modules/vx-easyui/vx-easyui-min.js"></script>
    <script src="http://www.jeasyui.com/node_modules/vx-easyui/babel.min.js"></script>
    <style type="text/css">
        body{
            padding: 20px;
        }
        h2{
            margin: 0 0 20px 0;
        }
        .mycontainer{
            min-width: 700px;
        }
    </style>
</head>
<body>
	    <div id="app"></div>
	    
	    
    <script type="text/babel">
var demopagination2={
	template: `
	<div>
		<h2>Pagination - Lazy Load</h2>
		<div style="margin-bottom:10px">
			<Label for="c1">Pager on: </Label>
			<ComboBox inputId="c1" style="width:120px"
				:data="pageOptions" 
				v-model="pagePosition" 
				:editable="false"
				:panelStyle="{height:'auto'}">
			</ComboBox>
		</div>
		<DataGrid style="height:450px"
				:pagination="true"
				:lazy="true"
				:data="data"
				:total="total"
				:loading="loading"
				:pageNumber="pageNumber"
				:pageSize="pageSize"
				groupField="group"
				:pagePosition="pagePosition"
				:pageLayout="['list','sep','first','prev','num','tpl','next','last','refresh','info']"
				@pageChange="onPageChange($event)">
			<GridColumn field="inv" title="Inv No"></GridColumn>
			<GridColumn field="name" title="Name"></GridColumn>
			<GridColumn field="amount" title="Amount" align="right"></GridColumn>
			<GridColumn field="price" title="Price" align="right"></GridColumn>
			<GridColumn field="cost" title="Cost" align="right"></GridColumn>
			<GridColumn field="note" title="Note"></GridColumn>
			      
			<template slot="group" slot-scope="scope" >
				<span style="font-weight:bold">
					{{scope.value}} 
				</span>
			</template>
		</DataGrid>
	</div>
	`,
			
	data() {
		return {
			total: 0,
			pageNumber: 1,
			pageSize: 10,
			data: [],
			loading: false,
			pagePosition: "bottom",
			pageOptions: [
				{ value: "bottom", text: "Bottom" },
				{ value: "top", text: "Top" },
				{ value: "both", text: "Both" }
			]
		};
	},
	created() {
		this.loadPage(this.pageNumber, this.pageSize);
	},
	methods: {
		onPageChange(event) {
			this.loadPage(event.pageNumber, event.pageSize);
		},
		loadPage(pageNumber, pageSize) {
			this.loading = true;
			setTimeout(() => {
				let result = this.getData(pageNumber, pageSize);
				this.total = result.total;
				this.pageNumber = result.pageNumber;
				this.data = result.rows;
				this.loading = false;
			}, 1000);
		},
		getData(pageNumber, pageSize) {
			let total = 100000;
			let data = [];
			let start = (pageNumber - 1) * pageSize;
			for (let i = start + 1; i <= start + pageSize; i++) {
				let amount = Math.floor(Math.random() * 1000);
				let price = Math.floor(Math.random() * 1000);
				data.push({
					group:'group',
					inv: "Inv No " + i,
					name: "Name " + i,
					amount: amount,
					price: price,
					cost: amount * price,
					note: "Note " + i
				});
			}
			return {
				total: total,
				pageNumber: pageNumber,
				pageSize: pageSize,
				rows: data
			};
		}
	}
}
var app = new Vue({
	el: '#app',
	template: `<demopagination2 class="mycontainer"></demopagination2>`,
	components: {
		demopagination2:demopagination2
	}
})
    </script>
    
</body>
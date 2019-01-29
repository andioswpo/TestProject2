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
    <script src="https://unpkg.com/axios/dist/axios.min.js"></script>
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
	    <div id="app">
	    	<orderitem class="mycontainer">

	    	</orderitem>
	    </div>
	    
	<script type="text/x-template" id="orderitem-template">
			    <div>
<MenuButton text="Help" :plain="true" iconCls="icon-help">
    <Menu>
        <MenuItem text="Help"></MenuItem>
        <MenuItem text="Update"></MenuItem>
        <MenuItem text="About"></MenuItem>
    </Menu>
</MenuButton>

			        <DataGrid :data="orderlist" :clickToEdit="true" editMode="row" 
						:pagination="true" 
						@rowDblClick="onRowDblClick($event)"
						style="height:250px">
			            <GridColumn field="id" title="Item ID" :editable="true"></GridColumn>
			            <GridColumn field="price" title="价格"></GridColumn>
			            <GridColumn field="name" title="标题" align="right"></GridColumn>
			            <GridColumn field="provinceName" title="省份" align="right"></GridColumn>
			            <GridColumn field="cityName" title="地区" width="30%"></GridColumn>
			            <GridColumn field="countyName" title="县市" align="center"></GridColumn>
			        </DataGrid>
			    </div>
    </script>

	    
    <script type="text/babel">
    Vue.component('orderitem',{
    	template: '#orderitem-template',
    	data() {
    		return {
    			orderlist:[],
				total:0
    		}
    	},
    	methods: {
			onRowDblClick(row) {
				alert(row.id);
			},
    		loadorderlist(page,rows)
    		{
    			var _this = this;
    			axios.get('/orderlist.action', {
    			    params: {
    			      page: page,
    				  rows: rows
    			    }
    			  })
    			  .then(function (response) {
    				  _this.total = response.data.total;
    				  _this.orderlist = response.data.rows;
    				  console.log('loadorderlist orderlist='+_this.orderlist.length);
    			});
    		}
    	},
    	beforeMount() {
    		console.log('beforeMount');
			
    	},
    	mounted() {
    		console.log('mounted orderlist='+this.orderlist.length);
    	},
    	created() {
    		this.loadorderlist(1,1);
    		console.log('created orderlist='+this.orderlist.length);
    	}

    });

var app = new Vue({
	el: '#app',
	data:{
			orderlist:[]
	},
	methods: {

	},
	beforeMount:function(){

	}
})



    </script>
    
</body>
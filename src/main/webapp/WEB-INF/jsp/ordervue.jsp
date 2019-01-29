<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>列表</title>
<link rel="stylesheet" type="text/css" href="/static/js/base.css">
<script type="text/javascript" src="/static/js/jquery.min.js"></script>
<script type="text/javascript" src="/static/js/base.js"></script>
<script src="https://cdn.jsdelivr.net/npm/vue/dist/vue.js"></script>
<script src="https://unpkg.com/axios/dist/axios.min.js"></script>
<script>
	function doadd()
	{
		$('#orderadd').css('display','block');
	}
	function doclose()
	{
		$('#orderadd').css('display','none');
	}
	function additem()
	{
		vue.order.items.push({
			id:'',
			prdtId:'1',
			uuid:Math.random()
		});
	}
	function openorder(id)
	{
		$('#orderadd').css('display','block');
		vue.loadorder(id);
	}
</script>
</head>

<body>
	<div id="app">
	<input type="button" value="添加" onclick="doadd()"/>
	<table>
		<tr>
			<th>全选</th>
			<th>名称</th>
			<th>时间</th>
			<th>金额</th>
			<th>省份</th>
			<th>地区</th>
			<th>县市</th>
		</tr>
		<tr v-for="o in orderlist">
			<td>{{o.id}}</td>
			<td><a v-bind:href="'javascript:openorder(\''+o.id+'\')'">{{o.name}}</a></td>
			<td>{{o.ts}}</td>
			<td>{{o.amount}}</td>
			<td>{{o.provinceName}}</td>
			<td>{{o.cityName}}</td>
			<td>{{o.countyName}}</td>
		</tr>
	</table>

	<div class="pdiv" v-html="pagehtml"></div>

	<div id="orderadd" style="display:none">
		<table>
			<tr>
				<td colspan="2">
					订单主表
					<input type="button" value="关闭" onclick="doclose()"/>
					<input type="button" value="添加订单明细" onclick="additem()"/>
					<input type="button" value="保存订单" v-on:click="saveorder()"/>
				</td>
			</tr>
			<tr>
				<td>名称</td>
				<td><input type="text" v-model="order.name"/></td>
			</tr>
			<tr>
				<td>时间</td>
				<td><input type="text" v-model="order.ts"/></td>
			</tr>
			<tr>
				<td>金额</td>
				<td><input type="text" v-model="order.amount"/></td>
			</tr>
			<tr>
				<td>省份</td>
				<td>
					<select v-model="order.provinceCode" v-on:change="selectCity()">
						<option v-for="p in provicelist" v-bind:value="p.code">{{p.name}}</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>地区</td>
				<td>
					<select v-model="order.cityCode" v-on:change="selectCounty()">
						<option v-for="p in citylist" :value="p.code">{{p.name}}</option>
					</select>
				</td>
			</tr>
			<tr>
				<td>县市</td>
				<td>
					<select v-model="order.countyCode">
						<option v-for="p in countylist" :value="p.code">{{p.name}}</option>
					</select>
				</td>
			</tr>
		</table>
		<!-- 使用VUE组件显示订单明细列表 -->
		<order-item2 v-bind:items="order.items"
			v-bind:prdtlist="prdtlist" v-bind:dodelete="dodelete" 
			inline-template>
				<table>
				<tr>
					<td colspan="10">
						订单明细
					</td>
				</tr>
				<tr>
					<th>主键</th>
					<th>产品</th>
					<th>数量</th>
					<th>价格</th>
					<th>金额</th>
					<th>操作</th>
				</tr>
				<tr v-for="oi in items">
					<td>{{oi.id}} {{oi.uuid}}</td>
					<td>
						<select v-model="oi.prdtId">
							<option v-for="p in prdtlist" :value="p.code">{{p.name}}</option>
						</select>
					</td>
					<td><input type="text" v-model="oi.num"/></td>
					<td><input type="text" v-model="oi.price"/></td>
					<td>{{oi.num*oi.price}}</td>
					<td><button v-on:click="dodelete(oi.uuid)">删除</button></td>
				</tr>
			</table>
		</order-item2>
	</div>
	
	</div>
	
	<script>
	//第一种VUE组件定义方式，使用字符串定义模板
	Vue.component('order-item',
		{
			//定义VUE组件的属性名称
		 	props: ['items','prdtlist'],
			template:
				'<table>'+
				'<tr>'+
				'	<td colspan="10">'+
				'		订单明细'+
				'	</td>'+
				'</tr>'+
				'<tr>'+
				'	<th>主键</th>'+
				'	<th>产品</th>'+
				'	<th>数量</th>'+
				'	<th>价格</th>'+
				'	<th>金额</th>'+
				'	<th>操作</th>'+
				'</tr>'+
				'<tr v-for="oi in items">'+//遍历数据模型
				'	<td>{{oi.id}}</td>'+
				'	<td>'+
				'		<select v-model="oi.prdtId">'+//绑定产品下拉框
				'			<option v-for="p in prdtlist" :value="p.code">{{p.name}}</option>'+
				'		</select>'+
				'	</td>'+
				'	<td><input type="text" v-model="oi.num"/></td>'+
				'	<td><input type="text" v-model="oi.price"/></td>'+
				'	<td>{{oi.num*oi.price}}</td>'+
				'	<td><button v-on:click="dodelete(oi.uuid)">删除</button></td>'+//绑定事件处理
				'</tr>'+
			'</table>	'
		}
	);

	Vue.component('order-item2',
		{
		 	props: ['items','prdtlist','dodelete']
		}
	);

	var vue = new Vue({
		el:'#app',
		data:{
			orderlist:{},
			order:{
				items:[]
			},
			provicelist:[],
			citylist:{},
			countylist:{},
			prdtlist:[{code:1,name:'手机'},{code:2,name:'笔记本'},{code:3,name:'数码相机'}],
			total:0,
			pagesize:1,
			pagehtml:''
		},
		methods:{
			selectCity:function()
			{
				var _this = this;
				axios.get('/city.action', {
				    params: {
				    	pcode: this.order.provinceCode
				    }
				  })
				  .then(function (response) {
					  _this.citylist = response.data;
					  _this.order.cityCode=_this.citylist[0].code;
						return axios.get('/county.action', {
						    params: {
						    	city: _this.order.cityCode
						    }
						  });
				})
				.then(function (countyData)
				{
					  _this.countylist = countyData.data;
					  _this.order.countyCode=_this.countylist[0].code;
				});
			},
			selectCounty:function()
			{
				var _this = this;
				axios.get('/county.action', {
				    params: {
				    	city: this.order.cityCode
				    }
				  })
				  .then(function (response) {
					  _this.countylist = response.data;
					  _this.order.countyCode=_this.countylist[0].code;
				})
			},
			loadaddress:function(province,city,county)
			{
				//PromiseAPI
				var _this = this;
				axios.get('/province.action')
				  .then(function (response) {
					  _this.provicelist = response.data;
					  
						//设置默认省份
						if(province=='' || province == null)
						{
							_this.order.provinceCode=_this.provicelist[0].code;
						}
						//根据默认省份装载地区
						return axios.get('/city.action', {
						    params: {
						    	pcode: _this.order.provinceCode
						    }
						  })
				  })
				 .then(function (response) {
					  _this.citylist = response.data;
					  
						//判断地区是否为空
						if(city == '' || city == null)
						{
							_this.order.cityCode = _this.citylist[0].code;
						}
						//装载县市
						return axios.get('/county.action', {
						    params: {
						    	city: _this.order.cityCode
						    }
						  })
				 })
				.then(function (response) {
					  _this.countylist = response.data;
					  if(county == '' || county == null)
						  _this.order.countyCode=_this.countylist[0].code;
				})
				.catch(function (error){
					alert(error)
				});
			
			},
			dodelete:function(id)
			{
				var len = this.order.items.length;
				for(var i=0;i<len;i++)
				{
					if(this.order.items[i].uuid == id)
					{
						this.order.items.splice(i,1);
					}
				}
			},
			saveorder:function()
			{
				var _this = this;
				axios.post('/ordersave.action', _this.order)
				  .then(function (response) {
					  _this.loadorderlist();
					  _this.loadorder(_this.order.id);
				})
			},
			loadorderlist:function(page,rows)
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

					  //_this.pagehtml = vuepagediv(_this.total,_this.pagesize,page,'vue.loadorderlist');
					  _this.pagehtml = pagedivjs('vue.loadorderlist',page,_this.pagesize,_this.total);
				});
			},
			loadorder:function(id)
			{
				var _this = this;//this 代表VUE实例对象
				axios.get('/loadorder.action', {
				    params: {
				      id: id
				    }
				  })
				  .then(function (response) {
					  _this.order = response.data;
					  _this.loadaddress(_this.order.provinceCode,_this.order.cityCode,
							  _this.order.countyCode);
				});
			}
		},
		beforeMount:function()
		{
			this.loadorderlist(1,1);
			this.loadaddress('','','');
		}
	})
	</script>
	
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>    
<!doctype html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
	<title>列表</title>
	<script src="http://code.angularjs.org/1.2.25/angular.min.js"></script>
	<link rel="stylesheet" type="text/css" href="/static/js/base.css">
</head>
  <body>
  	<!-- 定义Angular应用的根Dom对象 -->
  	<!-- 绑定Controller OrderCtrl -->
  	<!-- 初始化方法loadorderlist() -->
    <div ng-app="OrderApp" ng-controller="OrderCtrl" ng-init="loadorderlist()">
		<!-- 绑定 添加按钮事件 doadd()-->
		<input type="button" value="添加" ng-click="doadd()"/>
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
			<!-- 循环遍历数据对象 -->
			<!-- 点击每行触发openorder(o.id) -->
			<tr ng-repeat="o in orderlist" ng-click="openorder(o.id)">
				<!-- 插值绑定 -->
				<td>{{o.id}}</td>
				<td>{{o.name}}</td>
				<td>{{o.ts}}</td>
				<td>{{o.amount}}</td>
				<td>{{o.provinceName}}</td>
				<td>{{o.cityName}}</td>
				<td>{{o.countyName}}</td>
			</tr>
		</table>
		<!-- 订单输入窗口 -->
		<div id="orderadd" style="display:{{showorder}}">
			<table>
				<tr>
					<td colspan="2">
						<!-- 绑定按钮事件 -->
						订单主表
						<input type="button" value="关闭" ng-click="doclose()"/>
						<input type="button" value="添加订单明细" ng-click="additem()"/>
						<input type="button" value="保存订单" ng-click="saveorder()"/>
					</td>
				</tr>
				<tr>
					<td>名称</td>
					<!-- 绑定数据到Dom控件 -->
					<td><input type="text" ng-model="order.name"/></td>
				</tr>
				<tr>
					<td>时间</td>
					<td><input type="text" ng-model="order.ts"/></td>
				</tr>
				<tr>
					<td>金额</td>
					<td><input type="text" ng-model="order.amount"/></td>
				</tr>
				<tr>
					<td>省份</td>
					<td>
						<!-- 绑定省份数据、下拉事件 -->
						<select ng-model="order.provinceCode" ng-change="selectcity()"
							ng-options="p.code as p.name for p in provicelist">
							{{p.name}}</select>
					</td>
				</tr>
				<tr>
					<td>地区</td>
					<td>
						<!-- 绑定地区数据、下拉事件 -->
						<select ng-model="order.cityCode" ng-change="selectcounty()"
							ng-options="p.code as p.name for p in citylist">
							{{p.name}}</select>
					</td>
				</tr>
				<tr>
					<td>县市</td>
					<td>
						<!-- 绑定县市数据-->
						<select ng-model="order.countyCode" 
							ng-options="p.code as p.name for p in countylist">
							{{p.name}}</select>
					</td>
				</tr>
			</table>
			<!-- 订单明细  -->
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
				<!-- 遍历订单明细 -->
				<tr ng-repeat="oi in order.items">
					<td>{{oi.id}}</td>
					<td>
						<select ng-model="oi.prdtId"
							ng-options="p.code as p.name for p in prdtlist">
							{{p.name}}</select>
					</td>
					<td><input type="text" ng-model="oi.num"/></td>
					<td><input type="text" ng-model="oi.price"/></td>
					<td>{{oi.num*oi.price}}</td>
					<td><button ng-click="dodelete(oi.uuid)">删除</button></td>
				</tr>
			</table>
		</div>
    </div>
<script>
//定义模块
var myApp = angular.module('OrderApp', []);
//在模块OrderApp中定义Controller
//OrderCtrl中依赖注入'$scope','$http'
//构造方法必须包含此两个参数function($scope,$http)
myApp.controller('OrderCtrl', ['$scope','$http',
	function($scope,$http){
	//定义数据对象
	$scope.showorder = 'none';
	$scope.prdtlist=[{code:'1',name:'手机'},{code:'2',name:'笔记本'},{code:'3',name:'数码相机'}],
    //定义方法
    $scope.doclose = function() {
    	$scope.showorder = 'none';
    },
    $scope.doadd = function() {
    	$scope.showorder = 'block';
    },
	$scope.loadorderlist = function()
	{
    	//请求订单列表数据
		$scope.method = 'GET';
		$scope.url = '/orderlist.action';
		$http({method: $scope.method, url: $scope.url})
		.then(function(response) 
		{
			//赋值订单列表对象
			$scope.orderlist = response.data.rows;
		}, function(response) {
			$scope.orderlist = response.data || 'Request failed';
		});
	},
	//根据订单ID请求后台Order对象
	$scope.openorder = function(id)
	{
		$scope.method = 'GET';
		$scope.url = '/loadorder.action?id='+id;
		$http({method: $scope.method, url: $scope.url})
		.then(function(response) 
		{
			//赋值订单对象
			$scope.order = response.data;
			$scope.loadaddress($scope.order.provinceCode,
	        		  $scope.order.cotuCode,$scope.order.countyCode);
			$scope.showorder = 'block';
		}, function(response) {
			$scope.orderlist = response.data || 'Request failed';
		});
	},
	//选择省份时查询后台地区数据
	$scope.selectcity = function()
	{
		//加载地区
		$scope.method = 'GET';
		$scope.url = '/city.action?pcode='+$scope.order.provinceCode;
		$http({method: $scope.method, url: $scope.url})
		.then(function(cityResponse) 
		{
			$scope.citylist = cityResponse.data;
			$scope.order.cityCode=cityResponse.data[0].code;
			//根据默认地区选择县市列表
			city=cityResponse.data[0].code;
			$scope.selectcounty();
		});
	},
	//选择县市列表
	$scope.selectcounty = function()
	{
		//加载县市
		$scope.method = 'GET';
		$scope.url = '/county.action?city='+$scope.order.cityCode;
		$http({method: $scope.method, url: $scope.url})
		.then(function(countyResponse) 
		{
			$scope.countylist = countyResponse.data;
			$scope.order.countyCode=countyResponse.data[0].code;
		});
	},
	//点击添加订单调用的方法
	$scope.additem = function()
	{
		//向订单对象的明细对象添加默认数据
		$scope.order.items.push({
			id:'',
			prdtId:'1',
			num:1,
			price:1,
			uuid:Math.random()
		});
	},
	//删除订单明细调用的方法
	$scope.dodelete = function(id)
	{
		var len = $scope.order.items.length;
		for(var i=0;i<len;i++)
		{
			//循环遍历删除订单明细，根据uuid匹配选中删除
			if($scope.order.items[i] && $scope.order.items[i].uuid == id)
			{
				$scope.order.items.splice(i,1);
			}
		}
	},
	//保存订单方法
	$scope.saveorder = function()
	{
		$scope.method = 'POST';
		$scope.url = '/ordersave.action';
		$http({method: $scope.method, url: $scope.url,data:$scope.order})
		.then(function(response) 
		{
			$scope.order = response.data;
			$scope.loadorderlist();
			//$scope.openorder($scope.order.id);
		});
	},
	//根据参数选中地址下拉框
	$scope.loadaddress = function(province,city,county)
	{
		$scope.method = 'GET';
		$scope.url = '/province.action';
		$http({method: $scope.method, url: $scope.url})
		.then(function(response) 
		{
			$scope.provicelist = response.data;
			//设置默认省份
			if(province=='' || province == null)
			{
				$scope.order.provinceCode=response.data[0].code;
				province=response.data[0].code;
			}
			//加载地区
			$scope.method = 'GET';
			$scope.url = '/city.action?pcode='+province;
			$http({method: $scope.method, url: $scope.url})
			.then(function(cityResponse) 
			{
				$scope.citylist = cityResponse.data;
				//判断地区是否为空
				if(city == '' || city == null)
				{
					$scope.order.cityCode=cityResponse.data[0].code;
					city=cityResponse.data[0].code;
				}
				//加载县市
				$scope.method = 'GET';
				$scope.url = '/county.action?city='+city;
				$http({method: $scope.method, url: $scope.url})
				.then(function(countyResponse) 
				{
					$scope.countylist = countyResponse.data;
					//判断地区是否为空
					if(city == '' || city == null)
					{
						$scope.order.countyCode=countyResponse.data[0].code;
					}
				});
			});
			
		}, function(response) {
			$scope.orderlist = response.data || 'Request failed';
		});

	
	}
}]);
</script>
  </body>
</html>

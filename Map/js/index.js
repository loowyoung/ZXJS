$('#findCount').click(function() {
	var xc = $('#xc').val();
	var bx = $('#bx').val();
	var betax = $('#betax').val();
	$.ajax({
		type: "get",
		url: "http://localhost:8085/getCount",
		async: true,
		data: {
			xc: $('#xc').val(),
			bx: $('#bx').val(),
			betax: $('#betax').val()
		},
		success: function(res) {
			var point1 = [],
				point2 = [],
				point01 = [],
				point02 = [],
				point001 = [],
				point002 = [],
				point0001 = [],
				point0002 = [];
			//删除res[0]的内容
			while(res.length > 0) {
				var delnum = 0;
				$.each(res, function(index, item) {
					var del = res[0].x;
					if(item.x == del) {
						delnum = index;
					}
				});
				//新建数组，获取x轴相同数据，然后去判断浓度最接近0.1
				var arrT = res.slice(0, delnum + 1);
				//删除
				res.splice(0, delnum + 1);
				//循环arrT,取最接近0.01的浓度
				var min1 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min2 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min01 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min02 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min001 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min002 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min0001 = {
						x: 0,
						y: 0,
						nd: 0
					},
					min0002 = {
						x: 0,
						y: 0,
						nd: 0
					};
				$.each(arrT, function(index, item) {
					if(Math.abs(item.nd - 0.01) < 0.01 * 0.05 &&
						Math.abs(item.nd - 0.01) < Math.abs(min1.nd - 0.01) &&
						item.y < 0) {
						min1 = arrT[index];
					}
					if(Math.abs(item.nd - 0.01) < 0.01 * 0.05 &&
						Math.abs(item.nd - 0.01) < Math.abs(min2.nd - 0.01) &&
						item.y > 0) {
						min2 = arrT[index];
					}
					if(Math.abs(item.nd - 0.001) < 0.001 * 0.05 &&
						Math.abs(item.nd - 0.001) < Math.abs(min01.nd - 0.001) &&
						item.y < 0) {
						min01 = arrT[index];
					}
					if(Math.abs(item.nd - 0.001) < 0.001 * 0.05 &&
						Math.abs(item.nd - 0.001) < Math.abs(min02.nd - 0.001) &&
						item.y > 0) {
						min02 = arrT[index];
					}
					if(Math.abs(item.nd - 0.0001) < 0.0001 * 0.05 &&
						Math.abs(item.nd - 0.0001) < Math.abs(min01.nd - 0.0001) &&
						item.y < 0) {
						min001 = arrT[index];
					}
					if(Math.abs(item.nd - 0.0001) < 0.0001 * 0.05 &&
						Math.abs(item.nd - 0.0001) < Math.abs(min02.nd - 0.0001) &&
						item.y > 0) {
						min002 = arrT[index];
					}
					if(Math.abs(item.nd - 0.00001) < 0.00001 * 0.05 &&
						Math.abs(item.nd - 0.00001) < Math.abs(min01.nd - 0.00001) &&
						item.y < 0) {
						min0001 = arrT[index];
					}
					if(Math.abs(item.nd - 0.00001) < 0.00001 * 0.05 &&
						Math.abs(item.nd - 0.00001) < Math.abs(min02.nd - 0.00001) &&
						item.y > 0) {
						min0002 = arrT[index];
					}
				});
				//折线图小于0的坐标集合,0.01
				if(min1.x != 0) {
					var x = 116.41136 - (min1.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min1.y / 111000);
					point1.push(new T.LngLat(x, y));
				}
				//折线图大于0的坐标集合,0.01
				if(min2.x != 0) {
					var x = 116.41136 - (min2.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min2.y / 111000);
					point2.push(new T.LngLat(x, y));
				}
				//折线图小于0的坐标集合,0.001
				if(min01.x != 0) {
					var x = 116.41136 - (min01.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min01.y / 111000);
					point01.push(new T.LngLat(x, y));
				}
				//折线图大于0的坐标集合,0.001
				if(min02.x != 0) {
					var x = 116.41136 - (min02.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min02.y / 111000);
					point02.push(new T.LngLat(x, y));
				}
				//折线图小于0的坐标集合,0.0001
				if(min001.x != 0) {
					var x = 116.41136 - (min001.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min001.y / 111000);
					point001.push(new T.LngLat(x, y));
				}
				//折线图大于0的坐标集合,0.0001
				if(min002.x != 0) {
					var x = 116.41136 - (min002.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min002.y / 111000);
					point002.push(new T.LngLat(x, y));
				}
				//折线图小于0的坐标集合,0.00001
				if(min0001.x != 0) {
					var x = 116.41136 - (min0001.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min0001.y / 111000);
					point0001.push(new T.LngLat(x, y));
				}
				//折线图大于0的坐标集合,0.00001
				if(min0002.x != 0) {
					var x = 116.41136 - (min0002.x / (111000 * Math.cos(39.97569)));
					var y = 39.97569 + (min0002.y / 111000);
					point0002.push(new T.LngLat(x, y));
				}

			}
			var map;
			var zoom = 16;
			//初始化地图对象
			map = new T.Map("mapDiv");
			//设置显示地图的中心点和级别
			map.centerAndZoom(new T.LngLat(116.41136, 39.97569), zoom);

			//创建面对象
			//浓度为0.01的情况
			var point2R = point2.reverse();
			var point1point2 = point1.concat(point2R);
			var polygon1 = new T.Polygon(point1point2, {
				color: "red",
				weight: 3,
				opacity: 1,
				fillColor: "red",
				fillOpacity: 1
			});
			//浓度为0.001的情况
			var point02R = point02.reverse();
			var point01point02 = point01.concat(point02R);
			var polygon2 = new T.Polygon(point01point02, {
				color: "yellow",
				weight: 3,
				opacity: 1,
				fillColor: "yellow",
				fillOpacity: 1
			});
			//浓度为0.0001的情况
			var point002R = point002.reverse();
			var point001point002 = point001.concat(point002R);
			var polygon3 = new T.Polygon(point001point002, {
				color: "green",
				weight: 3,
				opacity: 1,
				fillColor: "green",
				fillOpacity: 1
			});
			//浓度为0.00001的情况
			var point0002R = point0002.reverse();
			var point0001point0002 = point0001.concat(point0002R);
			var polygon4 = new T.Polygon(point0001point0002, {
				color: "blue",
				weight: 3,
				opacity: 1,
				fillColor: "blue",
				fillOpacity: 1
			});
			//向地图上添加面
			map.addOverLay(polygon4);
			map.addOverLay(polygon3);
			map.addOverLay(polygon2);
			//			map.addOverLay(polygon1);

			//获取地图经纬度
			var cp = new T.CoordinatePickup(map, {
				callback: getLngLat
			})
			cp.addEvent();
		}
	});

});

function getLngLat(lnglat) {
	document.getElementById("lnglatStr").value = lnglat.lng.toFixed(6) + "," + lnglat.lat.toFixed(6);
}
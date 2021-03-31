package cn.oreo.common.util.common;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.common.util.constant.PageConstant;
import cn.oreo.common.util.entity.old.ArticleTopSO;
import cn.oreo.common.util.entity.old.OldUser;
import cn.oreo.common.util.entity.util.Distance;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.IntStream;

public class OreoUtil {

	private final static double EARTH_RADIUS = 6378.137;

	private static double rad(double d) {
		return d * Math.PI / 180.0;
	}

	/**
	 * 工具类
	 * 分页、模糊查询、排序、转下划线
	 * 作者：关明健
	 *
	 * @param queryRequest	查询条件
	 * @param object	需要查询对象
	 * @param isToUnderlineCase	是否开启驼峰转下划线
	 * @return
	 */
	public static void handleWrapperPageSort(QueryRequest queryRequest, QueryWrapper<?> queryWrapper, Object object, boolean isToUnderlineCase) {

		String field = queryRequest.getField();

		// 根据自定义字段排序
		if(StrUtil.isNotBlank(field)){

			// 驼峰转下划线
			if(isToUnderlineCase){
				field = StrUtil.toUnderlineCase(field);
			}

			// 排序
			if(StrUtil.isNotBlank(queryRequest.getOrder())){
				if("asc".equals(queryRequest.getOrder())){
					queryWrapper.orderByAsc(field);
				}else {
					queryWrapper.orderByDesc(field);
				}
			}else {
				// 默认升序排序
				queryWrapper.orderByAsc(field);
			}

		}else {
			// 默认使用id进行排序

			if(StrUtil.isNotBlank(queryRequest.getOrder())){
				if("asc".equals(queryRequest.getOrder())){
					queryWrapper.orderByAsc("id");
				}else {
					queryWrapper.orderByDesc("id");
				}
			}else {
				// 默认升序排序
				queryWrapper.orderByAsc("id");
			}
		}

		// 转 map，转下划线，忽略空值
		if(BeanUtil.isNotEmpty(object)){

			Map<String, Object> objectMap;

			if(isToUnderlineCase){
				objectMap = BeanUtil.beanToMap(object,true,true);
			}else {
				objectMap = BeanUtil.beanToMap(object,false,true);
			}

			if(MapUtil.isNotEmpty(objectMap)){
				// 模糊查询
				objectMap.forEach((key, value) -> {
					queryWrapper.like(key,value);
				});
			}
		}

	}

	/**
	 * 驼峰转下划线
	 *
	 * @param value 待转换值
	 * @return 结果
	 */
	public static String camelToUnderscore(String value) {
		if (StringUtils.isBlank(value)) {
			return value;
		}
		String[] arr = StringUtils.splitByCharacterTypeCamelCase(value);
		if (arr.length == 0) {
			return value;
		}
		StringBuilder result = new StringBuilder();
		IntStream.range(0, arr.length).forEach(i -> {
			if (i != arr.length - 1) {
				result.append(arr[i]).append("_");
			} else {
				result.append(arr[i]);
			}
		});
		return StringUtils.lowerCase(result.toString());
	}

	/**
	 * 封装前端分页表格所需数据
	 *
	 * @param pageInfo pageInfo
	 * @return Map<String, Object>
	 */
	public static Map<String, Object> getDataTable(IPage<?> pageInfo) {
		Map<String, Object> data = new HashMap<>(2);
		// 分页记录列表
		data.put(PageConstant.ROWS, pageInfo.getRecords());
		// 当前满足条件总行数
		data.put(PageConstant.TOTAL, pageInfo.getTotal());
		return data;
	}

	/**
	 * 对明文字符串进行MD5加密
	 * @param source 传入的明文字符串
	 * @return 加密结果
	 */
	public static String md5(String source) {

		// 1.判断source是否有效
		if(source == null || source.length() == 0) {

			// 2.如果不是有效的字符串抛出异常
			throw new RuntimeException(OreoConstant.MESSAGE_STRING_INVALIDATE);
		}

		try {
			// 3.获取MessageDigest对象
			String algorithm = "md5";

			MessageDigest messageDigest = MessageDigest.getInstance(algorithm);

			// 4.获取明文字符串对应的字节数组
			byte[] input = source.getBytes();

			// 5.执行加密
			byte[] output = messageDigest.digest(input);

			// 6.创建BigInteger对象
			int signum = 1;
			BigInteger bigInteger = new BigInteger(signum, output);

			// 7.按照16进制将bigInteger的值转换为字符串
			int radix = 16;
			String encoded = bigInteger.toString(radix).toUpperCase();

			return encoded;

		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}

		return null;
	}

	/**
	 * 判断当前请求是否为Ajax请求
	 * @param request 请求对象
	 * @return
	 * 		true：当前请求是Ajax请求
	 * 		false：当前请求不是Ajax请求
	 */
	public static boolean judgeRequestType(HttpServletRequest request) {

		// 1.获取请求消息头
		String acceptHeader = request.getHeader("Accept");
		String xRequestHeader = request.getHeader("X-Requested-With");

		// 2.判断
		return (acceptHeader != null && acceptHeader.contains("application/json"))

				||

				(xRequestHeader != null && xRequestHeader.equals("XMLHttpRequest"));
	}

	// 地图站点位置 距离排序
	public static List<cn.oreo.common.util.entity.old.Map> matchPoint(final double lng, final double lat, List<cn.oreo.common.util.entity.old.Map> mapList) {
		Collections.sort(mapList, (o1, o2) -> {
			double distance1 = GetDistance(lng, lat, o1.getLongitude(), o1.getLatitude());
			double distance2 = GetDistance(lng, lat, o2.getLongitude(), o2.getLatitude());
			if (distance1 < distance2) {
				return -1;    //正序
			} else if (distance1 == distance2) {
				return 0;
			} else {
				return 1;    //倒序
			}
		});
		return mapList;
	}

    // 去除标签
    public static String cutLabel(String str) {

		StringBuffer s = new StringBuffer();
		boolean flag = true;
		for(int i = 0; i < str.length(); i++) {
			char ch;
			ch = str.charAt(i);
			if('<' == ch) {
				flag = false;
			}
			if('>' == ch) {
				flag = true;
			}
			if(flag && ch != '>') {
				s.append(ch);
			}
		}
		return s.toString();
    }

    // 匹配最近的点
    public static cn.oreo.common.util.entity.old.Map matchNearestPoint(double lng, double lat, List<cn.oreo.common.util.entity.old.Map> mapList) {

		double min = Double.MAX_VALUE;

		cn.oreo.common.util.entity.old.Map s = null;

		for (cn.oreo.common.util.entity.old.Map map : mapList) {

			double distance = GetDistance(lng, lat, map.getLongitude(), map.getLatitude());

			if (distance < min) {
				min = distance;
				s = map;
			}
		}
		return s;
	}

	// 扫描半径距离内的点
	public static double GetDistance(double lng1, double lat1, double lng2, double lat2) {
		double radLat1 = rad(lat1);
		double radLat2 = rad(lat2);
		double a = radLat1 - radLat2;
		double b = rad(lng1) - rad(lng2);
		double s = 2 * Math.asin(Math.sqrt(
				Math.pow(Math.sin(a / 2), 2) + Math.cos(radLat1) * Math.cos(radLat2) * Math.pow(Math.sin(b / 2), 2)));
		s = s * EARTH_RADIUS;
		s = Math.round(s * 10000d) / 10000d;
		return s;
	}

	// 快速排序算法
    public static void quickSort(List<ArticleTopSO> list, int low, int high) {
		int l = low;
		int h = high;
		double baseNum = list.get(low).getRecomIndex();

		while (l < h) {
			//1.从右向左查找小于指定基数的数，找到之后跳出循环执行下面if循环，交换数据
			while (l < h && list.get(h).getRecomIndex() <= baseNum) {
				h--;
			}
			//交换数据
			if (l < h) {
				ArticleTopSO top;
				top = list.get(h);
				list.set(h, list.get(l));
				list.set(l, top);
				l++;
			}

			//2.从左向右查找大于指定基数的数，找到后跳出循环执行下面if循环，交换数据
			while (l < h && list.get(l).getRecomIndex() >= baseNum) {
				l++;
			}
			//交换数据
			if (l < h) {
				ArticleTopSO top;
				top = list.get(h);
				list.set(h, list.get(l));
				list.set(l, top);
				h--;
			}
		}
		if (l > low) {
			quickSort(list, low, l - 1);
		}
		if (h < high) {
			quickSort(list, l + 1, high);
		}
    }

	// 欧式距离计算
	public static double oudistance(OldUser user1, OldUser user2) {
		double temp = 0;
		double a = 0;
		double b = 0;

		if ("1".equals(user1.getSex())) {
			a = 1;
		} else {
			a = 2;
		}

		if ("1".equals(user2.getSex())) {
			b = 1;
		} else {
			b = 2;
		}
		System.out.println("a:" + a);
		System.out.println("b:" + b);
		temp = Math.pow((a - b)*10, 2) + Math.pow(user1.getAge() - user2.getAge(), 2);

		return Math.sqrt(temp);
	}

	// 找出最大频率
	public static String maxP(Map<String, Double> map) {
		String key = null;
		double value = 0.0;

		for (Map.Entry<String, Double> entry : map.entrySet()) {
			if (entry.getValue() > value) {
				key = entry.getKey();
				value = entry.getValue();
			}
		}
		return key;
	}

	// 计算频率
	public static Map<String, Double> computeP(Map<String, Integer> map, double k) {
		Map<String, Double> p = new HashMap<String, Double>();

		for (Map.Entry<String, Integer> entry : map.entrySet()) {
			p.put(entry.getKey(), entry.getValue() / k);
		}

		return p;
	}

	// 计算每个分类包含的点的个数
	public static Map<String, Integer> getNumberOfType(List<Distance> listDistance, List<OldUser> listOldUser, double k) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		int i = 0;

		System.out.println("选取的k个点，由近及远依次为：");
		for (Distance distance : listDistance) {
			System.out.println("id为" + distance.getId() + ",距离为：" + distance.getDisatance());
			long id = distance.getId();
			// 通过id找到所属类型,并存储到HashMap中
			for (OldUser user : listOldUser) {
				if (user.getId() == id) {
					if (map.get(user.getLabel()) != null) {
						map.put(user.getLabel(), map.get(user.getLabel()) + 1);
					}else {
						map.put(user.getLabel(), 1);
					}
				}
			}
			i++;
			if (i >= k) {
				break;
			}
		}
		return map;
	}
}

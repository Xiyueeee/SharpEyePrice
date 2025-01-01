# # 导入自动化模块
# from DrissionPage import ChromiumPage
# import csv
# import time
# import os
# import requests
# import base64
# from PIL import Image
# import pillow_avif_plugin
# from io import BytesIO
#
#
# # 图片格式转换函数
# def avif_to_jpg(avif_file_path, output_file_path):
#     try:
#         # 打开 .avif 文件并转化为 base64 格式
#         with open(avif_file_path, 'rb') as f:
#             avif_data = base64.b64encode(f.read()).decode('utf-8')
#
#         # Base64 数据前缀拼接
#         avif_data = f"data:image/avif;base64,{avif_data}"
#
#         # 将 Base64 数据解析
#         img_data = avif_data.split(',')[-1]
#         byte_data = base64.b64decode(img_data)
#
#         # 使用 BytesIO 对象包装字节数据
#         byte_stream = BytesIO(byte_data)
#
#         # 使用 pillow_avif 插件打开 AVIF 图片
#         image = Image.open(byte_stream)
#
#         # 将 AVIF 图片保存为 JPEG 格式
#         image.save(output_file_path, 'JPEG')
#         print(f"AVIF 图片已成功转换为 JPEG 格式并保存为 {output_file_path}")
#         return True
#     except Exception as e:
#         print(f"AVIF 转换失败: {e}")
#         return False
#
#
# # 创建 CSV 文件
# f = open('data.csv', mode='w', encoding='utf-8', newline='')
# csv_writer = csv.DictWriter(f, fieldnames=['标题', '价格', '图片', '平台', '链接'])
# csv_writer.writeheader()
#
# # 获取用户输入的关键词
# keyword = input("请输入商品关键词：")
#
# # 打开浏览器 (实例化浏览器对象)
# dp = ChromiumPage()
# dp.get('https://www.jd.com/')
# dp.ele('css:#key').input(keyword)
# dp.ele('css:.button').click()
#
# for page in range(1, 2):  # 可以调整爬取的页数
#     # 延时等待
#     time.sleep(2)
#     # 下滑页面
#     for _ in range(22):  # 可以根据需要调整滑动次数
#         dp.scroll.down(400)
#         time.sleep(1)
#
#     dp.scroll.to_bottom()
#     time.sleep(3)  # 等待页面完全加载
#
#     # 获取商品信息
#     lis = dp.eles('css:.gl-item')
#
#     for idx, li in enumerate(lis, 1):  # 为每个商品加上索引
#         # 提取商品数据信息
#         title = li.ele('css:.p-name a em').text  # 商品名字
#         price = li.ele('css:.p-price strong i').text  # 价格
#         img_url = li.ele('css:.p-img a img').attr('src')  # 图片链接
#         href = li.ele('css:.p-img a').attr('href')
#         # 补全图片 URL 协议
#         if img_url and img_url.startswith('//'):
#             img_url = 'https:' + img_url
#
#         # 图片存储
#         avif_img_name = f"JD_{keyword}_{idx}.avif"
#         jpg_img_name = avif_img_name.replace('.avif', '.jpg')
#         avif_img_path = os.path.join('frontend', 'static', 'imgs', avif_img_name)
#         jpg_img_path = os.path.join('frontend', 'static', 'imgs', jpg_img_name)
#
#         # 下载图片并保存为 .avif
#         try:
#             headers = {
#                 'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36',
#                 'Referer': 'https://www.jd.com/'
#             }
#             response = requests.get(img_url, headers=headers, timeout=10)
#             if response.status_code == 200:
#                 os.makedirs(os.path.dirname(avif_img_path), exist_ok=True)
#                 with open(avif_img_path, 'wb') as img_file:
#                     img_file.write(response.content)
#                 print(f"图片已保存: {avif_img_path}")
#             else:
#                 print(f"图片下载失败，状态码: {response.status_code}")
#                 avif_img_name = "下载失败"
#         except Exception as e:
#             print(f"图片下载失败: {img_url}, 错误: {e}")
#             avif_img_name = "下载失败"
#
#         # 转换 .avif 文件为 .jpg 文件
#         if avif_img_name != "下载失败" and avif_img_name.endswith('.avif'):
#             success = avif_to_jpg(avif_img_path, jpg_img_path)
#             if success:
#                 img_name = jpg_img_name
#             else:
#                 img_name = "转换失败"
#         else:
#             img_name = "下载失败"
#
#         # 保存数据
#         data = {
#             '标题': title,
#             '价格': price,
#             '图片': img_name,  # 保存转换后的图片名称
#             '平台': '京东',
#             '链接': href
#         }
#         csv_writer.writerow(data)
#         print(data)
#
#     # 点击下一页按钮
#     try:
#         dp.ele('css:.pn-next').click()
#     except:
#         print("没有找到下一页按钮，退出爬取")
#         break

# 导入自动化模块
from DrissionPage import ChromiumPage
import csv
import time
import os
import requests

# 创建 CSV 文件
f = open('data.csv', mode='w', encoding='utf-8', newline='')
csv_writer = csv.DictWriter(f, fieldnames=['标题', '价格', '图片', '平台', '链接'])
csv_writer.writeheader()

# 获取用户输入的关键词
keyword = input("请输入商品关键词：")

# 打开浏览器 (实例化浏览器对象)
dp = ChromiumPage()
dp.get('https://www.jd.com/')
dp.ele('css:#key').input(keyword)
dp.ele('css:.button').click()

for page in range(1, 2):  # 可以调整爬取的页数
    # 延时等待
    time.sleep(2)
    # 下滑页面
    for _ in range(22):  # 可以根据需要调整滑动次数
        dp.scroll.down(400)
        time.sleep(1)

    dp.scroll.to_bottom()
    time.sleep(3)  # 等待页面完全加载

    # 获取商品信息
    lis = dp.eles('css:.gl-item')

    for idx, li in enumerate(lis, 1):  # 为每个商品加上索引
        # 提取商品数据信息
        title = li.ele('css:.p-name a em').text  # 商品名字
        price = li.ele('css:.p-price strong i').text  # 价格
        img_url = li.ele('css:.p-img a img').attr('src')  # 图片链接
        href = li.ele('css:.p-img a').attr('href')
        # 补全图片 URL 协议
        if img_url and img_url.startswith('//'):
            img_url = 'https:' + img_url

        # 图片存储
        jpg_img_name = f"JD_{keyword}_{idx}.jpg"
        jpg_img_path = os.path.join('frontend', 'static', 'imgs', jpg_img_name)

        # 下载图片并保存为 .jpg
        try:
            headers = {
                'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/96.0.4664.110 Safari/537.36',
                'Referer': 'https://www.jd.com/'
            }
            response = requests.get(img_url, headers=headers, timeout=10)
            if response.status_code == 200:
                os.makedirs(os.path.dirname(jpg_img_path), exist_ok=True)
                with open(jpg_img_path, 'wb') as img_file:
                    img_file.write(response.content)
                print(f"图片已保存: {jpg_img_path}")
            else:
                print(f"图片下载失败，状态码: {response.status_code}")
                jpg_img_name = "下载失败"
        except Exception as e:
            print(f"图片下载失败: {img_url}, 错误: {e}")
            jpg_img_name = "下载失败"

        # 保存数据
        data = {
            '标题': title,
            '价格': price,
            '图片': jpg_img_name,  # 保存下载后的图片名称
            '平台': '京东',
            '链接': href
        }
        csv_writer.writerow(data)
        print(data)

    # 点击下一页按钮
    try:
        dp.ele('css:.pn-next').click()
    except:
        print("没有找到下一页按钮，退出爬取")
        break

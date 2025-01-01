import time
from selenium import webdriver
from selenium.webdriver.common.by import By
import csv
import os
import requests

# 获取用户输入的搜索关键词
keyword = input("请输入搜索关键词: ")

# 拼接网址
url = f'https://search.suning.com/{keyword}/'

# 创建CSV文件并写入标题行
output_csv = '苏宁.csv'
with open(output_csv, mode='w', encoding='utf-8', newline='') as f:
    csv_writer = csv.writer(f)
    csv_writer.writerow(['title', 'price', 'href', 'img_name', 'platform'])  # 添加平台字段

# 打开谷歌浏览器
driver = webdriver.Chrome()

# 打开拼接好的网址
driver.get(url)

# 创建图片保存路径
img_dir = 'frontend/static/imgs'
os.makedirs(img_dir, exist_ok=True)

# 初始化序号
n = 1

# 爬取数据，假设爬取33页
for page in range(1):  # 可调整页数
    time.sleep(2)
    print(f"正在爬取第 {page + 1} 页数据...")

    # 提取商品信息
    divs = driver.find_elements(By.CSS_SELECTOR, '.product-box')
    for div in divs:
        try:
            # 提取商品信息
            title = div.find_element(By.CSS_SELECTOR, '.title-selling-point').text
            price = div.find_element(By.CSS_SELECTOR, '.price-box').text
            img_url = div.find_element(By.CSS_SELECTOR, '.img-block img').get_attribute('src')
            href = div.find_element(By.CSS_SELECTOR, '.title-selling-point a').get_attribute("href")

            # 下载商品图片
            img_name = f"SN_{keyword}_{n}.jpg"
            img_path = os.path.join(img_dir, img_name)
            try:
                img_data = requests.get(img_url).content
                with open(img_path, 'wb') as img_file:
                    img_file.write(img_data)
                print(f"图片已保存: {img_path}")
            except Exception as e:
                print(f"图片下载失败: {img_url}, 错误: {e}")
                img_name = "下载失败"  # 如果下载失败，标记为下载失败

            # 保存数据到CSV文件
            with open(output_csv, mode='a', encoding='utf-8', newline='') as f:
                csv_writer = csv.writer(f)
                csv_writer.writerow([title, price, href, img_name, '苏宁'])  # 添加平台信息
                print(f"已保存数据: {title}, {price}, {href}, {img_name}, 苏宁")

            n += 1  # 更新序号
        except Exception as e:
            print(f"数据提取失败: {e}")

    # 点击下一页
    try:
        next_button = driver.find_element(By.CSS_SELECTOR, '#nextPage')
        if next_button.is_enabled():  # 检查下一页按钮是否可用
            next_button.click()
        else:
            print("没有下一页了，停止爬取。")
            break
    except Exception as e:
        print(f"下一页点击失败: {e}")
        break

    time.sleep(2)  # 等待页面加载

# 关闭浏览器
driver.quit()
print(f"爬取完成，数据已保存到 {output_csv}")

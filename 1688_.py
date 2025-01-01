import time
import os
import requests
from selenium import webdriver
from selenium.webdriver.common.by import By
import json
import urllib.parse

def search_products(keyword):
    # URL 编码处理
    encoded_keyword = urllib.parse.quote(keyword)

    # 拼接网址
    url = f'https://p4psearch.1688.com/page.html?hpageId=old-sem-pc-list&keywords={encoded_keyword}&cosite=baidujj_pz&location=re&trackid=885662561117990122602&spm=a2638t.b_30496503.szyx_head.submit&keywordid=&bt=&exp=pcCpxGuessExp%3AA&ptid=hr1066c8189143a1'
    print(url)
    driver = webdriver.Chrome()
    driver.get(url)
    # 启动 Selenium WebDriver
    driver = webdriver.Chrome()

    # 打开拼接好的网址
    driver.get(url)

    # 等待页面加载
    driver.execute_script("window.scrollBy(0, 1000);")  # 向下滚动1000像素
    time.sleep(1)  # 等待加载
    driver.execute_script("window.scrollBy(0, 1000);")
    time.sleep(1)

    # 提取数据
    divs = driver.find_elements(By.CSS_SELECTOR, '.offer_exp')
    count = 0
    products = []

    for div in divs:
        if count >= 30:
            break

        title = div.find_element(By.CSS_SELECTOR, '.offer-title span').text  # 商品名称
        price = div.find_element(By.CSS_SELECTOR, '.offer-price span').text  # 商品价格
        img_url = div.find_element(By.CSS_SELECTOR, '.offer-img img').get_attribute('src')  # 商品图片URL
        href = div.find_element(By.CSS_SELECTOR, '.new_ui_offer.offer_item a').get_attribute('href')
        # 获取商品描述（这里我们将title作为商品描述）
        detail = title  # 将标题作为商品的描述

        # 生成图片文件名作为 img_guid
        img_data = requests.get(img_url).content
        img_name = f"{keyword}_1688_img{count + 1}.jpg"
        img_guid = img_name  # 使用文件名作为唯一标识符
        img_path = os.path.join('frontend', 'static', 'imgs', img_name)

        # 如果图片保存目录不存在，创建目录
        img_dir = os.path.dirname(img_path)
        if not os.path.exists(img_dir):
            os.makedirs(img_dir)

        with open(img_path, 'wb') as f_img:
            f_img.write(img_data)

        print(f"图片已保存: {img_path}")

        # 保存商品信息
        product = {
            'name': keyword,
            'detail': detail,
            'price': price,
            'img_guid': img_guid,
            'href': href
        }
        products.append(product)
        count += 1
        print(title, price,  href)

    # 关闭浏览器
    driver.quit()

    # 返回抓取的产品信息列表
    return products

if __name__ == "__main__":
    # 获取用户输入的关键词
    keyword = input("请输入关键词: ")

    # 执行搜索并获取结果
    products = search_products(keyword)

    # 将产品信息转换为 JSON 格式输出
    # print(json.dumps(products, ensure_ascii=False, indent=4))

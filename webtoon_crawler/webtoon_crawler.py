# -*- coding: utf-8 -*-
from __future__ import unicode_literals
import os
import requests
from bs4 import BeautifulSoup
from IPython.display import Image
from PIL import Image as PILImage
from multiprocessing import Pool

import mysql.connector
from mysql.connector import Error

from selenium import webdriver

driver = webdriver.Chrome('C:\\Users\\gther\\Downloads\\chromedriver_win32\\chromedriver.exe')
driver.get('https://nid.naver.com/nidlogin.login')
driver.implicitly_wait(3)
driver.find_element_by_name('id').send_keys('gther2486')
driver.find_element_by_name('pw').send_keys('@skehwlwhs159')
driver.find_element_by_xpath('//*[@id="frmNIDLogin"]/fieldset/input').click()


connection = None
cursor = None

def readyDB():
    global connection
    global cursor
    connection = mysql.connector.connect(host='localhost',
                                             database='modoowebtoon',
                                             user='root',
                                             password='root')
    try: 
        cursor = connection.cursor()
    except mysql.connector.Error as error:
        print("Failed inserting BLOB data into MySQL table {}".format(error))

def getURL():
    url = 'https://comic.naver.com/webtoon/weekday.nhn'
    driver.get(url)
    # 네이버 웹툰 페이지 들어갈 때
    #html = requests.get(url).text
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')

    # 요일 당 웹툰 개수
    cnt_of_webtoon = [0 for i in range(7)]

    for col_index, col_inner in enumerate(soup.select('.col_inner')):
        # 요일 추출
        day_of_the_week = col_inner.select('h4 span')[0].text
        # 요일 당 웹툰 개수
        cnt_of_webtoon[col_index] = len(col_inner.select('.thumb'))

        for thumb_index, thumb_tag in enumerate(col_inner.select('.thumb')):
            # 각 웹툰 썸네일 URL
            if len(thumb_tag.select('a')[0].contents) == 6 and thumb_tag.select('a')[0].contents[4]['class'][0] == 'mark_adult_thumb':
                continue
            thumb_url = 'https://comic.naver.com' + thumb_tag.contents[1]['href']
            # 썸네일 자체 URL
            thumb_data_url = thumb_tag.contents[1].contents[1]['src']

            # 각 웹툰 들어갈 때
            #driver.get(thumb_url)
            html = requests.get(thumb_url).text
            soup = BeautifulSoup(html, 'html.parser')


            recently_url = ""
            # 각 웹툰 최신화 URL
            for content in soup.select('td a'):
                if content['href'].find('/webtoon') != -1:
                    recently_url = 'https://comic.naver.com' + content['href']
                    break
            recently_url = recently_url[:recently_url.rfind('&')]
            # 최신화가 몇화인지
            recnetly_num = (int)(recently_url[recently_url.rfind('=') + 1:])

            crawl_naver_webtoon(recently_url, day_of_the_week)


def getURLD():
    url = 'https://comic.naver.com/webtoon/weekday.nhn'
    # 네이버 웹툰 페이지 들어갈 때
    driver.get(url)
    html = driver.page_source
    soup = BeautifulSoup(html, 'html.parser')

    # 요일 당 웹툰 개수
    cnt_of_webtoon = [0 * 7]

    for col_index, col_inner in enumerate(soup.select('.col_inner')):
        # 요일 추출
        day_of_the_week = col_inner.select('h4 span')[0].text
        # 요일 당 웹툰 개수
        cnt_of_webtoon[col_index] = len(col_inner.select('.thumb'))

        for thumb_index, thumb_tag in enumerate(col_inner.select('.thumb')):
            # 각 웹툰 썸네일 URL
            thumb_url = 'https://comic.naver.com' + thumb_tag.contents[1]['href']
            # 썸네일 자체 URL
            thumb_data_url = thumb_tag.contents[1].contents[1]['src']
            
            # 각 웹툰 들어갈 때
            driver.get(thumb_url)
            html = driver.page_source
            soup = BeautifulSoup(html, 'html.parser')

            
            recently_url = ""
            # 각 웹툰 최신화 URL
            for content in soup.select('td a'):
                if content['href'].find('/webtoon') != -1:
                    recently_url = 'https://comic.naver.com' + content['href']
                    break
            recently_url = recently_url[:recently_url.rfind('&')]
            # 최신화가 몇화인지
            recnetly_num = (int)(recently_url[recently_url.rfind('=') + 1:])

            for index in range(recnetly_num, 1, -1):
                target_url = recently_url[:recently_url.rfind('=') + 1] + str(index)
                # 웹툰 각 화 들어갈 때
                #crawl_naver_webtoon(target_url, day_of_the_week)

def crawl_naver_webtoon(episode_url, day_of_the_week):
    #driver.get(episode_url)
    html = requests.get(episode_url).text
    soup = BeautifulSoup(html, 'html.parser')

    #image_list = []
    #full_width, full_height = 0, 0

    # 작가 추출
    author_name = soup.select('span.wrt_nm')[0].text.strip()
    
    # 웹툰 제목 추출
    comic_title = str(soup.select('.comicinfo h2')[0].contents[0])
    print(comic_title)
    # 에피소드 제목 추출
    ep_title = soup.select('.tit_area h3')[0].text

    #for img_tag in soup.select('.wt_viewer img'):
    #    image_file_url = img_tag['src']
    #    headers = {'Referer': episode_url}
        # 이미지 추출
    #    image_file_data = requests.get(image_file_url, headers=headers).content
        # db에 저장
    #    insertdb(day_of_the_week, comic_title, ep_title, author_name, image_file_data)

        # 파일로 저장
        #open(image_file_path, 'wb').write(image_file_data)
       
    print('Completed !')

def insertdb(day_of_the_week, title, semi_title, author, image_data):
    try: 
        sql_insert_blob_query = """ INSERT INTO naver_serial_webtoon
                            (day_of_the_week, title, semi_title, author, image_data) VALUES (%s,%s,%s,%s,%s)"""

        # Convert data into tuple format
        insert_blob_tuple = (day_of_the_week, title, semi_title, author, image_data)
        result = cursor.execute(sql_insert_blob_query, insert_blob_tuple)
        connection.commit()
        #print("Image and file inserted successfully as a BLOB into python_employee table", result)

    except mysql.connector.Error as error:
        print("Failed inserting BLOB data into MySQL table {}".format(error))
    

def closeDB():
    if (connection.is_connected()):
        cursor.close()
        connection.close()


if __name__ == '__main__':

    #pool = Pool(processes=4) # 4개의 프로세스를 사용합니다.
    #pool.map(getURL, range(10)) # pool에 일을 던져줍니다.
    #readyDB()
    getURL()
    #closeDB()

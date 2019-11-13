# -*- coding: utf-8 -*-
from __future__ import unicode_literals
import os
import requests
from bs4 import BeautifulSoup
from IPython.display import Image
from PIL import Image as PILImage
from multiprocessing import Pool


def crawl_naver_webtoon(episode_url):
    html = requests.get(episode_url).text
    soup = BeautifulSoup(html, 'html.parser')

    image_list = []
    full_width, full_height = 0, 0


    # 웹툰 제목, 작가 추출
    comic_title = ' '.join(soup.select('.comicinfo h2')[0].text.split())
    # 에피소드 제목 추출
    ep_title = ' '.join(soup.select('.tit_area h3')[0].text.split())

    for img_tag in soup.select('.wt_viewer img'):
        image_file_url = img_tag['src']
        image_dir_path = os.path.join(os.path.dirname(__file__), comic_title, ep_title)
        image_file_path = os.path.join(image_dir_path, os.path.basename(image_file_url))

        if not os.path.exists(image_dir_path):
            os.makedirs(image_dir_path)

        #print(image_file_path)

        headers = {'Referer': episode_url}
        # 이미지 추출
        image_file_data = requests.get(image_file_url, headers=headers).content
        # 파일로 저장
        open(image_file_path, 'wb').write(image_file_data)
        '''with open(image_file_path, 'wb') as f:
            f.write(image_file_data)
            im = PILImage.open(image_file_path)
            width, height = im.size
            image_list.append(im)
            full_width = max(full_width, width)
            full_height += height
                
    canvas = PILImage.new('RGB', (full_width, full_height), 'white')
    output_height = 0
    
    for im in image_list:
        width, height = im.size
        canvas.paste(im, (0, output_height))
        output_height += height
    
    canvas.save('merged.jpg')'''

    print('Completed !')

def getURL():
    url = 'https://comic.naver.com/webtoon/weekday.nhn'

    html = requests.get(url).text
    soup = BeautifulSoup(html, 'html.parser')

    for thumb_tag in soup.select('.thumb'):
        # 각 웹툰 썸네일 URL
        thumb_url = 'https://comic.naver.com' + thumb_tag.contents[1]['href']

        html = requests.get(thumb_url).text
        soup = BeautifulSoup(html, 'html.parser')
        # 각 웹툰 최신화 URL
        recently_url = 'https://comic.naver.com' + soup.select('td')[2].contents[1]['href']
        recently_url = recently_url[:recently_url.rfind('&')]
        # 최신화가 몇화인지
        recnetly_num = (int)(recently_url[recently_url.rfind('=') + 1:])

        for index in range(recnetly_num, 1, -1):
            target_url = recently_url[:recently_url.rfind('=') + 1] + str(index)
            crawl_naver_webtoon(target_url)



if __name__ == '__main__':

    #pool = Pool(processes=4) # 4개의 프로세스를 사용합니다.
    #pool.map(getURL, range(10)) # pool에 일을 던져줍니다.

    getURL()

    #episode_url = 'http://comic.naver.com/webtoon/detail.nhn?titleId=20853&no=1048&weekday=tue'
    #crawl_naver_webtoon(episode_url)
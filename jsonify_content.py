from dep import *

# BS contents ->>  [indexed dictionary, ...]
def jsonify_content(content):
    
    # BS.contents는 html 태그 안의 모든 first child를 리스트로 반환
    # 태그가 없어도 무방하므로 본문 내용까지 별도의 child로 리턴됨
    # 기사 앞뒤의 빈 칸(탭, 줄바꿈 기호 등)을 잘라내고 br 태그를 제거함
    content_list = content.contents
    x = list(filter(lambda s : s != "<br/>", map(lambda s: str(s).strip(), content_list)))
    
    # 각 object에 기사 요소의 배치 순서를 지시
    index = 0
    
    # json_list에 각 기사 요소를 순서대로 담아 리턴
    
    json_list = []
    # 텍스트 타입에 따라 분류
    # type : text, video, image, link
    for i in x:

        # 잘못 나온 놈은 걸러야 한다. 인덱스 추가 안하고 다음 요소 탐색
        if i == "":
            continue

        # 여기부턴 어쨌든 요소에 속함.
        # HTML 태그가 하나도 없으면 텍스트로 분류
        if not "<" in i: 
            json_list.append({'ArticleIndex': index, "ArticleType": "text", 'content' : i})

        # 태그가 있는 요소들은 다음과 같이 분류함:
        else: 
            bs = BS(i, 'html.parser')

            # 비디오 태그 : To Be Implemented
            if "<video" in i:
                json_list.append({'ArticleIndex': index, "ArticleType": "video", 'content': i})
            
            # 이미지 태그 : 대부분 span으로 묶여 있음. 사진과 태그 포함
            # 다만 다른 형식에 span이 사용될 수 있으므로 img를 이용함
            elif "<img" in i:
                img_url = bs.find('img').get('data-src')
                img_tag = bs.find('em', class_='img_desc')
                # 이미지 태그가 없으면 빈 스트링을 저장함.
                if img_tag: img_tag = img_tag.text
                else: img_tag = ""
                json_list.append({'ArticleIndex': index, "ArticleType": "image", 'content': img_url, 'tag' : img_tag})
                

            # 링크 태그 : a 안에 들어 있음
            # 네이버 기사는 본문 안에 링크 삽입 잘 안하므로 a 안의 요소만 따도 무방
            elif "<a" in i: 
                link_url = bs.find('a').get('href')
                link_text = bs.find('a').text
                json_list.append({'ArticleIndex': index, "ArticleType": "link", 'url': link_url, 'content': link_text})

            # 소제목 태그 : strong, em 등의 텍스트 요소를 포함함
            elif any(["<font" in i, "<strong" in i, "<b" in i]): 
                json_list.append({'ArticleIndex': index, "ArticleType": "strapline", 'content': bs.text})

            # 가끔 텍스트에 꺾쇠를 집어넣는 똥같은 언론사들이 있음
            # 문과가 또...
            # 이 경우는 텍스트로 분류
            else:
                json_list.append({'ArticleIndex': index, "ArticleType": "text", 'content': bs.text})

        #print(index)
        index += 1
    return json_list
  

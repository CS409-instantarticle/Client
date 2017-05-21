from dep import *

# get_text : URL(str) ->> BS element
def get_text(URL):
    source_code_from_URL = str(urllib.request.urlopen(URL).read(), "utf-8").replace("<br>", "<br/>").replace("<br/><br/>", "<br/>")
    soup = BS(source_code_from_URL, 'html.parser')
    result = soup.find_all('div', id="dic_area")

    # entertain 뉴스를 레퍼런스로 하는 경우 별도의 핸들링 필요
    if not result:
        #print(URL)
        source_code_from_URL = str(urllib.request.urlopen(
            URL.replace("m.news.naver.com", "m.entertain.naver.com")).read(), "utf-8").replace("<br>", "<br/>").replace("<br/><br/>", "<br/>")
        soup = BS(source_code_from_URL, 'html.parser')
        result = soup.find_all('div', id="contentArea")
    
    return result[0]


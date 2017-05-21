from dep import *
from NewsArticle import *

def request_list(k):
    data = {"page": str(k)}
    r = requests.post(url, headers=headers, data=data)
    data = json.loads(r.text)
    articleList = []
    for j in range(12):
        # data의 데이터 선택 및 article 클래스 생성
        articleData = data["message"]["itemList"][j]
        article = NewsArticle()
        # 주요 항목 삽입
        article.ArticleID = articleData["articleId"]
        article.ArticleTitle = articleData["title"]
        article.ArticleDate = articleData["standardFullDate"]
        article.ThumbnailImageURL = str(articleData["imageUrl"])
        article.Video = articleData["videoType"]
        article.Press = articleData["officeName"]
        article.Link = "http://m.news.naver.com" + articleData["linkUrl"]
        article.SectionName = (
            articleData["sectionName"] if articleData["sectionName"] else None)
        article.get_contents()
        articleList.append(article)
    return articleList

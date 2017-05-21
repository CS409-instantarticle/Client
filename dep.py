from bs4 import BeautifulSoup as BS
import urllib.request
import requests
import json
import urllib
import sqlite3
from flask import Flask, request, session, g, redirect, url_for, \
             abort, render_template, flash
import datetime
from multiprocessing import Process   

url = "http://m.news.naver.com/mainNews/moreMainNews.json"

headers = {
    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
    'Host': 'm.news.naver.com',
    'Origin': 'http://m.news.naver.com',
    'Referer': 'http://m.news.naver.com/',
    'X-Requested-With': 'XMLHttpRequest'
}



from dep import *
from request_list import *
import sys

# 크롤링 테스트


def main(overwrite, maxrequest):
    maxrequest = int(maxrequest)
    flag = True
    i = 0

    while flag and (i < maxrequest):
        for j in request_list(i):
            try:
                j.write()
            except IOError:
                j.write()
                if overwrite: 
                    flag = False
        print(i)
        i = i + 1

    print("Done")

if __name__ == "__main__":
    l = sys.argv
    if len(l) == 2:
        main(*sys.argv)
    elif len(l) == 1:
        main(eval(sys.argv[0]), 415)
    else:
        main(True, 415)

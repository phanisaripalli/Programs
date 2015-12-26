__author__ = 'phani'

def generate_word_lists(words) :
    min_line_length = 3

    if not (min_line_length > len(words)):
        if min_line_length > len(words):
            min_line_length = len(words)

    word_list = []
    for x in range(min_line_length -1, len(words) + 1, 1):
        if x + 2 <= len(words):
            word_list.append(words[0:x])

    return word_list

print generate_word_lists("I am foo bar he she a".split())

class Message:
    def __init__(self, message):
        self.words = message.split()
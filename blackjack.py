import os
import random
import pandas as pd
import time
import subprocess
import threading

'''
#only uncomment if running for first time on machine
past_games = pd.read_csv("ens_a_data_1m.csv")
df = past_games[['player_actual_points', 'visible_dealer_points', 'player_action', 'game_result']].copy()
df.to_csv('new_table.csv', ',')
print("made new table")
df = pd.read_csv("new_table.csv")
df.to_csv('final_table.csv',',', header='False')

df = df.groupby(["player_actual_points","visible_dealer_points","player_action", "game_result"]).size()


df = pd.read_csv("final_table.csv")
df = df.groupby(["player_actual_points","visible_dealer_points","player_action",'game_result']).nunique()
#cols = [5,6,7,8,9]
#df.drop(df.columns[cols],axis=1,inplace=True)
df.to_csv("last_table.csv", header = 'False')
'''
df = pd.read_csv("last_table.csv")

def get_hit_wins(actual_points, visible_points):
    for index, row in df.iterrows():
        if row['player_actual_points'] == actual_points and row["visible_dealer_points"] == visible_points and row["game_result"] == "W" and row["player_action"] == "H":
            return row['Unnamed: 0']

def get_stay_wins(actual_points, visible_points):
    for index, row in df.iterrows():
        if row['player_actual_points'] == actual_points and row["visible_dealer_points"] == visible_points and row["game_result"] == "W" and row["player_action"] == "S":
            return row['Unnamed: 0']


def get_hit_losses(actual_points, visible_points):
    for index, row in df.iterrows():
        if row['player_actual_points'] == actual_points and row["visible_dealer_points"] == visible_points and row["game_result"] == "L" and row["player_action"] == "H":
            return row['Unnamed: 0']

def get_stay_losses(actual_points, visible_points):
    for index, row in df.iterrows():
        if row['player_actual_points'] == actual_points and row["visible_dealer_points"] == visible_points and row["game_result"] == "L" and row["player_action"] == "S":
            return row['Unnamed: 0']

def send(msg):
    f = open("pipe.txt", "w")
    f.write(msg)
    f.close()           

last_turn = 0

def clearpy():
    f = open("pypipe.txt", "w")
    f.write("")
    f.close()

def read():
    global last_turn
    s = ''
    while len(s) == 0:
        f = open("pypipe.txt", "r")
        s = f.readline()
        f.close()
    clearpy()
    if s == 'q':
        clearpy()
        exit()
    return s[1:]

def thread_function():
    subprocess.run(["javac", "*.java"])
    subprocess.run(["java", "Controller"])

x = threading.Thread(target=thread_function, args=())
x.start()

time.sleep(3)
send("WELCOME TO BLACKJACK\n")
print("\nWELCOME TO BLACKJACK!\n")
time.sleep(.5)

send("Enter number of decks to use\n")
decks = read()
while not decks.isdigit():
    decks = read()

# user chooses number of decks of cards to use
deck = [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14] * (int(decks) * 4)

# initialize scores
wins = 0
losses = 0
tokens = 100
bet = 0

def str_1(hand, opp_hand):
    try:
        hit_ratio = get_hit_wins(total(hand), total(opp_hand)) / get_hit_losses(total(hand), total(opp_hand))
        stay_ratio = get_stay_wins(total(hand), total(opp_hand)) / get_hit_losses(total(hand), total(opp_hand))
        if(total(hand)) < 17:
            return True
        if hit_ratio > stay_ratio:
            return True
    except:
        return False


def place_bet():
    send("How much would you like to bet?")
    bet = read()
    while not bet.isdigit():
        bet = read()
    bet = int(bet)
    return bet

def set_tokens():
    return 100

def deal(deck):
    hand = []
    for i in range(2):
        random.shuffle(deck)
        card = deck.pop()
        if card == 11: card = "J"
        if card == 12: card = "Q"
        if card == 13: card = "K"
        if card == 14: card = "A"
        hand.append(card)
    return hand

def play_again():
    send("Do you want to play again? (Y/N)")
    time.sleep(.5)
    again = read().lower()
    if again == "y":
        dealer_hand = []
        player_hand = []
        deck = [2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14] * 4
        game()
    else:
        send("Bye!")
        time.sleep(.5)
        send("q")
        time.sleep(.5)
        send("")
        clearpy()
        print("Bye!")
        exit()


def total(hand):
    total = 0
    for card in hand:
        if card == "J" or card == "Q" or card == "K":
            total += 10
        elif card == "A":
            if total >= 11:
                total += 1
            else:
                total += 11
        else:
            total += card
    return total


def hit(hand):
    card = deck.pop()
    if card == 11: card = "J"
    if card == 12: card = "Q"
    if card == 13: card = "K"
    if card == 14: card = "A"
    hand.append(card)
    return hand


def clear():
    if os.name == 'nt':
        os.system('CLS')
    if os.name == 'posix':
        os.system('clear')


def print_results(dealer_hand, player_hand):
    clear()
    print("-" * 30 + "\n")
    print("    \033[1;32;40mWINS:  \033[1;37;40m%s   \033[1;31;40mLOSSES:  \033[1;37;40m%s\n" % (wins, losses))
    print("-" * 30 + "\n")
    send("dealer:The dealer has a " + str(dealer_hand) + " for a total of " + str(total(dealer_hand)))
    time.sleep(.5)
    send("You have a " + str(player_hand) + " for a total of " + str(total(player_hand)))
    time.sleep(.5)
    print("The dealer has a " + str(dealer_hand) + " for a total of " + str(total(dealer_hand)))
    print("You have a " + str(player_hand) + " for a total of " + str(total(player_hand)))


def blackjack(dealer_hand, player_hand):
    global wins
    global losses
    global tokens
    global bet


    if total(player_hand) == 21:
        print_results(dealer_hand, player_hand)
        send("Congratulations! You got a Blackjack!")
        time.sleep(.5)
        print("Congratulations! You got a Blackjack!\n")
        wins += 1
        tokens+=bet
        bet  = 0
        play_again()
    elif total(dealer_hand) == 21:
        print_results(dealer_hand, player_hand)
        send("Sorry, you lose. The dealer got a blackjack.")
        time.sleep(.5)
        print("Sorry, you lose. The dealer got a blackjack.\n")
        losses += 1
        tokens-=bet
        bet = 0
        play_again()


def score(dealer_hand, player_hand):
    # score function now updates to global win/loss variables
    global wins
    global losses
    global tokens
    global bet
    if total(player_hand) == 21:
        print_results(dealer_hand, player_hand)
        send("Congratulations! You got a Blackjack!")
        time.sleep(.5)
        print("Congratulations! You got a Blackjack!\n")
        tokens += bet
        bet = 0
        wins += 1
    elif total(dealer_hand) == 21:
        print_results(dealer_hand, player_hand)
        send("Sorry, you lose. The dealer got a blackjack.")
        time.sleep(.5)
        print("Sorry, you lose. The dealer got a blackjack.\n")
        tokens -= bet
        bet = 0
        losses += 1
    elif total(player_hand) > 21:
        print_results(dealer_hand, player_hand)
        send("Sorry. You busted. You lose.")
        time.sleep(.5)
        print("Sorry. You busted. You lose.\n")
        tokens -= bet
        bet = 0
        losses += 1
    elif total(dealer_hand) > 21:
        print_results(dealer_hand, player_hand)
        send("Dealer busts. You win!")
        time.sleep(.5)
        print("Dealer busts. You win!\n")
        tokens += bet
        bet = 0
        wins += 1
    elif total(player_hand) < total(dealer_hand):
        print_results(dealer_hand, player_hand)
        send("Sorry. Your score isn't higher than the dealer. You lose.")
        time.sleep(.5)
        print("Sorry. Your score isn't higher than the dealer. You lose.\n")
        tokens -= bet
        bet = 0
        losses += 1
    elif total(player_hand) > total(dealer_hand):
        print_results(dealer_hand, player_hand)
        send("Congratulations. Your score is higher than the dealer. You win!")
        time.sleep(.5)
        print("Congratulations. Your score is higher than the dealer. You win!\n")
        tokens += bet
        wins += 1
        bet = 0
        
def game():
    global wins
    global losses
    global tokens
    global bet

    clear()
    send(str(wins)+";"+str(losses))
    time.sleep(.5)
    print("-" * 30 + "\n")
    print("    \033[1;32;40mWINS:  \033[1;37;40m%s   \033[1;31;40mLOSSES:  \033[1;37;40m%s\n" % (wins, losses))
    print("-" * 30 + "\n")
    dealer_hand = deal(deck)
    player_hand = deal(deck)
    send("You have " + str(tokens) + " tokens" + "\n")
    time.sleep(.5)
    print("You have " + str(tokens) + " tokens")
    bet = place_bet()
    send("dealer:The dealer is showing a " + str(dealer_hand[0]) + "\n")
    time.sleep(.5)
    send("you:You have a " + str(player_hand) + " for a total of " + str(total(player_hand)) + "\n")
    time.sleep(.5)
    print("The dealer is showing a " + str(dealer_hand[0]))
    print("You have a " + str(player_hand) + " for a total of " + str(total(player_hand)))
    blackjack(dealer_hand, player_hand)
    quit = False
    while not quit:
        send("Do you want to [H]it, [S]tand, or [Q]uit?")
        choice = read().lower()
        if choice == 'h':
            hit(player_hand)
            send("you:" + str(player_hand))
            time.sleep(.5)
            send("Hand total: " + str(total(player_hand)))
            time.sleep(.5)
            print(player_hand)
            print("Hand total: " + str(total(player_hand)))
            if total(player_hand) > 21:
                send("You busted")
                print('You busted')
                tokens -= bet
                losses += 1
                time.sleep(.5)
                play_again()
        elif choice == 's':
            while str_1(dealer_hand, player_hand[:1]):
                hit(dealer_hand)
                send("dealer:" + str(dealer_hand))
                time.sleep(.5)
                print(dealer_hand)
                if total(dealer_hand) > 21:
                    send("Dealer busts, you win!")
                    print('Dealer busts, you win!')
                    wins += 1
                    tokens+=bet
                    time.sleep(.5)
                    play_again()
            score(dealer_hand, player_hand)
            time.sleep(.5)
            play_again()
        elif choice == "q":
            send("Bye!")
            time.sleep(.5)
            send("q")
            time.sleep(.5)
            send("")
            clearpy()
            print("Bye!")
            quit = True
            exit()


if __name__ == "__main__":
   game()

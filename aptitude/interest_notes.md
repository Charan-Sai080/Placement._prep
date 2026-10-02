# Aptitude Masterclass: Simple & Compound Interest

The goal of this document is to completely replace the need to memorize formulas. If you understand the underlying mechanics of how money grows, you can derive the answer to any problem using pure intuition.

## 1. The Core Intuition: What is Interest?
Think of interest as **"rent" for money**. 
When you deposit money in a bank, the bank uses it to give loans. In return, they pay you rent. 
*   **Principal (P):** The original amount of money.
*   **Rate (R):** The percentage of the Principal paid as rent per year.

There are two completely different rulesets for calculating this rent:

---

## 2. Simple Interest (SI): "The Straight Line"

### The Mental Model
In Simple Interest, **you only ever earn interest on your original Principal.** The bank ignores any interest you've already earned.
Because of this, **the amount of interest you earn is identical every single year.**

*Example: Deposit Rs. 100 at 10% per year.*
*   Year 1: Earn Rs. 10. (Total = 110)
*   Year 2: Earn Rs. 10. (Total = 120)
*   Year 3: Earn Rs. 10. (Total = 130)

### The "1-Year Percentage" Trick (Crucial for SI)
If a problem says: *"A sum of money doubles in 8 years at Simple Interest. What is the rate?"*
Do not use a formula. Use logic:
1. "Doubles" means you earned exactly **100%** of your original money in pure interest.
2. If you earned 100% over 8 years, and SI is identical every year...
3. You earned $\frac{100\%}{8} = \mathbf{12.5\% \text{ per year}}$. (That is your Rate!)

---

## 3. Compound Interest (CI): "The Snowball"

### The Mental Model
In Compound Interest, **you earn interest on your Principal AND on all the interest you've previously earned.** It is "interest on interest."

*Example: Deposit Rs. 100 at 10% per year.*
*   Year 1: Earn 10% of 100 = Rs. 10. (New Total = **110**)
*   Year 2: Earn 10% of the *new total* (110) = Rs. 11. (New Total = **121**)
*   Year 3: Earn 10% of the *new total* (121) = Rs. 12.10. (New Total = **133.10**)

Notice how the interest paid grows every year? (Rs. 10 -> Rs. 11 -> Rs. 12.10). 

### The "Scaling Multiplier" Trick (Crucial for CI)
Instead of calculating interest and adding it back, think in terms of a multiplier. 
If an investment grows by 10%, its new value is **110%** of what it was yesterday. 
In decimals, multiplying by **1.10** applies the interest instantly.

If you leave money in the bank for 3 years at 10%, the math is simply:
$\text{Principal} \times 1.10 \times 1.10 \times 1.10 = \text{Final Amount}$

### Variations in Compounding
Banks sometimes play tricks with *when* they compound the money.
*   **Half-Yearly Compounding:** The bank pays you twice a year. Therefore, you must cut the annual Rate in half, but you get paid twice as often. (e.g., 10% per year becomes 5% every 6 months).
*   **Fractional Years (e.g., 1.5 years):** For the full year, you earn Compound Interest. For the remaining 6 months, that new balance earns Simple Interest for that specific fraction of the year.

---

## 4. The Golden Rule: SI vs CI
*   **For the 1st year**, Simple Interest and Compound Interest are **exactly the same**. (Because there is no "previous interest" to earn interest on yet).
*   The difference only starts appearing in Year 2, when CI starts paying "interest on the Year 1 interest".

package com.comp5590.utils;

import java.util.concurrent.ThreadLocalRandom;

/**
 * The NameUtils class provides utility methods for working with names.
 * It provides methods for generating random first and last names.
 */
public class NameUtils {

    // generate 100 of each type of name, mainly british but also any country
    private static final String[] FIRST_NAMES = {
        "James",
        "John",
        "Robert",
        "Michael",
        "William",
        "David",
        "Richard",
        "Joseph",
        "Charles",
        "Thomas",
        "Daniel",
        "Matthew",
        "Anthony",
        "Mark",
        "Paul",
        "Steven",
        "Andrew",
        "Kenneth",
        "George",
        "Joshua",
        "Kevin",
        "Brian",
        "Edward",
        "Ronald",
        "Timothy",
        "Jason",
        "Jeffrey",
        "Ryan",
        "Jacob",
        "Gary",
        "Nicholas",
        "Eric",
        "Stephen",
        "Jonathan",
        "Larry",
        "Justin",
        "Scott",
        "Brandon",
        "Benjamin",
        "Samuel",
        "Gregory",
        "Frank",
        "Raymond",
        "Alexander",
        "Patrick",
        "Jack",
        "Dennis",
        "Jerry",
        "Tyler",
        "Aaron",
        "Jose",
        "Henry",
        "Adam",
        "Douglas",
        "Nathan",
        "Peter",
        "Zachary",
        "Kyle",
        "Walter",
        "Harold",
        "Jeremy",
        "Ethan",
        "Carl",
        "Keith",
        "Roger",
        "Gerald",
        "Vincent",
        "Arthur",
        "Terry",
        "Sean",
        "Christian",
        "Bryan",
        "Roy",
        "Louis",
        "Eugene",
        "Russell",
        "Randy",
        "Philip",
        "Harry",
        "Vincent",
        "Mildred",
        "Dorothy",
        "Betty",
        "Helen",
        "Margaret",
        "Ruth",
        "Virginia",
        "Frances",
        "Marie",
        "Alice",
        "Irene",
        "Evelyn",
        "Jane",
        "Doris",
        "Jean",
        "Ann",
        "Gloria",
        "Martha",
        "Lois",
        "Thelma",
        "Norma",
        "Eleanor",
        "Patricia",
        "Joan",
        "Mildred",
    };

    private static final String[] LAST_NAMES = {
        "Smith",
        "Johnson",
        "Williams",
        "Jones",
        "Brown",
        "Davis",
        "Miller",
        "Wilson",
        "Moore",
        "Taylor",
        "Anderson",
        "Thomas",
        "Jackson",
        "White",
        "Harris",
        "Martin",
        "Thompson",
        "Garcia",
        "Martinez",
        "Robinson",
        "Clark",
        "Rodriguez",
        "Lewis",
        "Lee",
        "Walker",
        "Hall",
        "Allen",
        "Young",
        "Hernandez",
        "King",
        "Wright",
        "Lopez",
        "Hill",
        "Scott",
        "Green",
        "Adams",
        "Baker",
        "Gonzalez",
        "Nelson",
        "Carter",
        "Mitchell",
        "Perez",
        "Roberts",
        "Turner",
        "Phillips",
        "Campbell",
        "Parker",
        "Evans",
        "Edwards",
        "Collins",
        "Stewart",
        "Sanchez",
        "Morris",
        "Rogers",
        "Reed",
        "Cook",
        "Morgan",
        "Bell",
        "Murphy",
        "Bailey",
        "Rivera",
        "Cooper",
        "Richardson",
        "Cox",
        "Howard",
        "Ward",
        "Torres",
        "Peterson",
        "Gray",
        "Ramirez",
        "James",
        "Watson",
        "Brooks",
        "Kelly",
        "Sanders",
        "Price",
        "Bennett",
        "Wood",
        "Barnes",
        "Ross",
        "Henderson",
        "Coleman",
        "Jenkins",
        "Perry",
        "Powell",
        "Long",
        "Patterson",
        "Hughes",
        "Flores",
        "Washington",
        "Butler",
        "Simmons",
        "Foster",
        "Gonzales",
        "Bryant",
        "Alexander",
        "Russell",
        "Griffin",
        "Diaz",
        "Hayes",
    };

    private static final String[] EMAIL_DOMAINS = {
        "gmail.com",
        "yahoo.com",
        "hotmail.com",
        "outlook.com",
        "icloud.com",
        "aol.com",
        "protonmail.com",
        "zoho.com",
        "yandex.com",
        "mail.com",
        "gmx.com",
        "tutanota.com",
        "fastmail.com",
        "hushmail.com",
        "runbox.com",
        "disroot.org",
        "riseup.net",
        "mailbox.org",
        "posteo.de",
        "kolabnow.com",
        "startmail.com",
        "countermail.com",
        "ctemplar.com",
        "scryptmail.com",
        "privatemail.com",
        "elude.in",
        "keemail.me",
        "mailfence.com",
        "mailbox.org",
        "protonmail.ch",
        "protonmail.com",
        "protonmail.net",
        "tutanota.com",
        "disroot.org",
    };

    private static final ThreadLocalRandom RANDOM = ThreadLocalRandom.current();

    /**
     * useful method for getting a random first name
     *
     * @return a random first name
     */
    public static String getRandomFirstName() {
        int index = RANDOM.nextInt(FIRST_NAMES.length);
        return FIRST_NAMES[index];
    }

    /**
     * useful method for getting a random last name
     *
     * @return a random last name
     */
    public static String getRandomLastName() {
        int index = RANDOM.nextInt(LAST_NAMES.length);
        return LAST_NAMES[index];
    }

    /**
     * useful method for getting a random email domain
     *
     * @return a random email domain
     */
    public static String getRandomEmailDomain() {
        int index = RANDOM.nextInt(EMAIL_DOMAINS.length);
        return EMAIL_DOMAINS[index];
    }

    // Generate random email
    public static String getRandomFullEmail() {
        return String.format(
            "%s.%s_%s@%s",
            NameUtils.getRandomFirstName().toLowerCase(),
            NameUtils.getRandomLastName().toLowerCase(),
            StringUtils.randomString(5, 10),
            NameUtils.getRandomEmailDomain()
        );
    }

    // Generate random email with custom first and last name
    public static String getRandomFullEmail(String firstName, String lastName) {
        return String.format(
            "%s.%s_%s@%s",
            firstName.toLowerCase(),
            lastName.toLowerCase(),
            StringUtils.randomString(5, 10),
            NameUtils.getRandomEmailDomain()
        );
    }
}

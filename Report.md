# Report: Three Ways to Send a Login Message

### Method 1: Plain String Format

**What are the pros and cons of using a plain string like `"LOGIN|user|pass"`?**

Honestly, this method is super straightforward and easy to implement. You just send a simple string with fields separated by a delimiter (`|` in this case). It’s easy to read and debug since it’s plain text. But the downside is that if the username or password accidentally contains the delimiter, your parsing will break and mess things up. Also, this approach is very basic — it can’t handle complex or nested data, so it’s not very scalable if you want to add more info later.

---

**How would you parse it, and what happens if the delimiter appears in the data?**

To parse it, you just split the string by the delimiter like this:

```java
String[] parts = message.split("\|");
```

Then you take the pieces based on their position. But if the data itself contains the `|` character, splitting will mess up the parts and your program won’t know what to do. For example, if someone’s password was `my|secret`, the split would create extra parts and break your logic.

---

**Is this approach suitable for more complex or nested data?**

Not really. For simple stuff, it’s fine. But if you want to send objects with nested fields, lists, or optional values, this method gets messy very fast. You’ll have to invent your own escaping rules or complicated parsing logic, which is a pain.

---

### Method 2: Serialized Java Object

**What’s the advantage of sending a full Java object?**

The big plus here is you can send the whole object exactly as it is, including all its data and structure, without worrying about parsing or formatting strings manually. On the receiving side, you just deserialize the object back to its original form. It’s neat and very Java-friendly.

---

**Could this work with a non-Java client like Python?**

Unfortunately no. Java serialization uses a special binary format that only Java understands. So if your client or server isn’t Java, this won’t work. Other languages don’t have built-in support for Java’s serialized objects, so it’s basically Java-to-Java only.

---

### Method 3: JSON

**Why is JSON often preferred for communication between different systems?**

It’s easy to read and write, supports nested data naturally, and is supported by pretty much every programming language out there. Because it’s plain text, it’s easy to debug and works perfectly for communication between different platforms and languages.

---

**Would this format work with servers or clients written in other languages?**

Yes! JSON is designed exactly for that. Whether your client is in Python, JavaScript, Java, or anything else, it can easily encode or decode JSON. That makes it super flexible and ideal for cross-platform communication.

---

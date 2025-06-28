package org.example

import javax.xml.namespace.QName

// 1. กำหนด data class สำหรับเก็บข้อมูลสินค้า
data class Product(val name: String, val price: Double, val category: String)

fun main() {
    // 2. สร้างรายการสินค้าตัวอย่าง (List<Product>)
    // สินค้า name = "Laptop", price = 35000.0, category = "Electronics"
    // สินค้า name = "Smartphone", price = 25000.0, category = "Electronics"
    // สินค้า name = "T-shirt", price = 450.0, category = "Apparel"
    // สินค้า name = "Monitor", price = 7500.0, category = "Electronics"
    // สินค้า name = "Keyboard", price = 499.0, category = "Electronics" // ราคาไม่เกิน 500
    // สินค้า name = "Jeans", price = 1200.0, category = "Apparel"
    // สินค้า name = "Headphones", price = 1800.0, category = "Electronics" // ตรงตามเงื่อนไข
    val products = listOf(
        Product("Laptop", 35000.0, "Electronics"),
        Product("Smartphone", 25000.0, "Electronics"),
        Product("T-shirt", 450.0, "Apparel"),
        Product("Monitor", 7500.0, "Electronics"),
        Product("Keyboard", 499.0, "Electronics"),
        Product("Jeans", 1200.0, "Apparel"),
        Product("Headphones", 1800.0, "Electronics")
    )

    println("รายการสินค้าทั้งหมด:")
    products.forEach { println(it) }
    println("--------------------------------------------------")

    // --- โจทย์: จงหาผลรวมราคาสินค้าทั้งหมดในหมวด 'Electronics' ที่มีราคามากกว่า 500 บาท ---

    // 3. วิธีที่ 1: การใช้ Chaining กับ List โดยตรง
    val totalElecPriceOver500 = products
        .filter { it.category == "Electronics" } // กรองสินค้าหมวด Electronics
        .filter { it.price > 500.0 } // กรองสินค้าที่ราคามากกว่า 500
        .map { it.price } // ดึงเฉพาะราคาออกมาเป็น List<Double>
        .sum() // หาผลรวมของราคา

    println("วิธีที่ 1: ใช้ Chaining กับ List")
    println("ผลรวมราคาสินค้า Electronics ที่ราคา > 500 บาท: $totalElecPriceOver500 บาท")
    println("--------------------------------------------------")


    // 4. (ขั้นสูง) วิธีที่ 2: การใช้ .asSequence() เพื่อเพิ่มประสิทธิภาพ
    val totalElecPriceOver500Sequence = products
        .asSequence()     // แปลง List เป็น Sequence ก่อนเริ่มประมวลผล
        .filter { it.category == "Electronics" }
        .filter { it.price > 500.0 }
        .map { it.price }
        .sum()


    println("วิธีที่ 2: ใช้ .asSequence() (ขั้นสูง)")
    println("ผลรวมราคาสินค้า Electronics ที่ราคา > 500 บาท: $totalElecPriceOver500Sequence บาท")
    println("--------------------------------------------------")


    println("อภิปรายความแตกต่างระหว่าง List และ Sequence:")
    println("1. List Operations (วิธีที่ 1):")
    println("   - ทุกครั้งที่เรียกใช้ operation (เช่น filter, map) จะมีการสร้าง Collection (List) ใหม่ขึ้นมาเพื่อเก็บผลลัพธ์ของขั้นนั้นๆ")
    println("   - ตัวอย่าง: filter ครั้งแรกสร้าง List ใหม่ -> filter ครั้งที่สองสร้าง List ใหม่อีกใบ -> map สร้าง List สุดท้าย -> sum ทำงาน")
    println("   - เหมาะกับข้อมูลขนาดเล็ก เพราะเข้าใจง่าย แต่ถ้าข้อมูลมีขนาดใหญ่มาก (ล้าน records) จะสิ้นเปลืองหน่วยความจำและเวลาในการสร้าง Collection ใหม่ๆ ซ้ำซ้อน")
    println()
    println("2. Sequence Operations (วิธีที่ 2):")
    println("   - ใช้การประมวลผลแบบ 'Lazy' (ทำเมื่อต้องการใช้ผลลัพธ์จริงๆ)")
    println("   - operations ทั้งหมด (filter, map) จะไม่ทำงานทันที แต่จะถูกเรียงต่อกันไว้")
    println("   - ข้อมูลแต่ละชิ้น (each element) จะไหลผ่าน Pipeline ทั้งหมดทีละชิ้น จนจบกระบวนการ")
    println("   - เช่น: 'Laptop' จะถูก filter category -> filter price -> map price จากนั้น 'Smartphone' ถึงจะเริ่มทำกระบวนการเดียวกัน")
    println("   - จะไม่มีการสร้าง Collection กลางทาง ทำให้ประหยัดหน่วยความจำและเร็วกว่ามากสำหรับชุดข้อมูลขนาดใหญ่ เพราะทำงานกับข้อมูลทีละชิ้นและทำทุกขั้นตอนให้เสร็จในรอบเดียว")
    println("   - การคำนวณจะเกิดขึ้นเมื่อมี 'Terminal Operation' มาเรียกใช้เท่านั้น (ในที่นี้คือ .sum())")


//2.2

    val PriceRange = products.groupBy {
        when {
            it.price <= 1000.0 -> "ราคาไม่เกิน 1,000 บาท"
            it.price in 1000.0..9999.9 -> "1,000-9,999"
            else -> "ราคา 10,000 ขึ้นไป"
        }
    }

    println("กลุ่มสินค้าตามราคา: ")
    PriceRange.forEach { (range, products) ->

        println("กลุ่มราคา $range:")
        products.forEach { println(" - ${it.name} (${it.price} บาท)") }
    }

}
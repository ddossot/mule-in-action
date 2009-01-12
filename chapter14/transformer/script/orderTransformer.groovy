import groovy.xml.MarkupBuilder

writer = new StringWriter()
builder = new MarkupBuilder(writer)

builder.orders() {
    payload.split('\n').each {line ->
        def fields = line.split(',')
        order() {
            subscriberId(fields[0])
            productId(fields[1])
            status(fields[2])
        }
    }
}
return writer.toString()

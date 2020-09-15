package com.romtn.springexample.disruptor;

public class ExampleEvent {

    private long id;

    public long getId() {
        return id;
    }

    public ExampleEvent setId(long id) {
        this.id = id;
        return this;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("ExampleEvent{");
        sb.append("id=").append(id);
        sb.append('}');
        return sb.toString();
    }
}

package client.core.model;

public interface IEventHandler<T> {
	public T handle(Event event);
}

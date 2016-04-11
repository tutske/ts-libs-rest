package org.tutske.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.tutske.options.OptionStore;
import org.tutske.rest.NotFoundHandler;
import org.tutske.rest.RoutingHandler;
import org.tutske.rest.UrlRouter;


public class Beans {

	private final int port = OptionStore.get (Options.PORT);
	private final String staticFilesPath = OptionStore.get (Options.STATIC_PATH);

	public Server server () {
		Server server = new Server (port);
		server.setHandler (handlers ());
		return server;
	}

	public HandlerList handlers () {
		HandlerList handlers = new HandlerList ();
		handlers.addHandler (resourceHandler ());
		handlers.addHandler (routingHandler ());
		handlers.addHandler (notFoundHandler ());
		return handlers;
	}

	public Handler resourceHandler () {
		String path = Main.class.getClassLoader ()
			.getResource (staticFilesPath)
			.toExternalForm ();

		ResourceHandler resources = new ResourceHandler ();
		resources.setResourceBase (path);

		return resources;
	}

	public Handler notFoundHandler () {
		return new NotFoundHandler (gson ());
	}

	public Handler routingHandler () {
		return new RoutingHandler (router (), gson ());
	}

	public UrlRouter router () {
		return new Routes ().router;
	}

	public Gson gson () {
		return new GsonBuilder ().create ();
	}

}

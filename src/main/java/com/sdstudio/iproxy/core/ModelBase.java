package com.sdstudio.iproxy.core;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class ModelBase {
	private PropertyChangeSupport propertyChangeSupport;

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		getPropertyChangeSupport().addPropertyChangeListener(listener);
	}

	public void addPropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		getPropertyChangeSupport().addPropertyChangeListener(propertyName,
				listener);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index,
			boolean oldValue, boolean newValue) {
		getPropertyChangeSupport().fireIndexedPropertyChange(propertyName,
				index, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index,
			int oldValue, int newValue) {
		getPropertyChangeSupport().fireIndexedPropertyChange(propertyName,
				index, oldValue, newValue);
	}

	protected void fireIndexedPropertyChange(String propertyName, int index,
			Object oldValue, Object newValue) {
		getPropertyChangeSupport().fireIndexedPropertyChange(propertyName,
				index, oldValue, newValue);
	}

	protected void firePropertyChange(PropertyChangeEvent evt) {
		getPropertyChangeSupport().firePropertyChange(evt);
	}

	protected void firePropertyChange(String propertyName, boolean oldValue,
			boolean newValue) {
		getPropertyChangeSupport().firePropertyChange(propertyName, oldValue,
				newValue);
	}

	protected void firePropertyChange(String propertyName, int oldValue,
			int newValue) {
		getPropertyChangeSupport().firePropertyChange(propertyName, oldValue,
				newValue);
	}

	protected void firePropertyChange(String propertyName, Object oldValue,
			Object newValue) {
		getPropertyChangeSupport().firePropertyChange(propertyName, oldValue,
				newValue);
	}

	public boolean hasListeners(String propertyName) {
		return getPropertyChangeSupport().hasListeners(propertyName);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		getPropertyChangeSupport().removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName,
			PropertyChangeListener listener) {
		getPropertyChangeSupport().removePropertyChangeListener(propertyName,
				listener);
	}

	protected void setPropertyChangeSupport(
			PropertyChangeSupport propertyChangeSupport) {
		this.propertyChangeSupport = propertyChangeSupport;
	}

	protected PropertyChangeSupport getPropertyChangeSupport() {
		if (propertyChangeSupport == null)
			propertyChangeSupport = new PropertyChangeSupport(this);
		return propertyChangeSupport;
	}

}

package nl.runnable.alfresco.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.alfresco.repo.dictionary.DictionaryDAO;
import org.alfresco.repo.dictionary.M2Model;
import org.alfresco.repo.dictionary.M2Namespace;
import org.alfresco.service.cmr.dictionary.DictionaryException;
import org.alfresco.service.namespace.QName;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.util.Assert;

/**
 * Registers and unregisters {@link M2Model}s with a {@link DictionaryDAO}. This class is stateful and intended to
 * support dynamic loading of models.
 * 
 * @author Laurens Fridael
 * 
 */
public class ModelRegistrar {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/* Dependencies */

	private DictionaryDAO dictionaryDao;

	/* Configuration */

	private List<M2Model> models = Collections.emptyList();

	/* State */

	private final List<QName> registeredModelNames = new ArrayList<QName>();

	/* Main operations */

	public void registerModels() {
		final Map<String, M2Model> namespaceProviders = new HashMap<String, M2Model>();
		final List<M2Model> modelsToRegister = getModels();
		for (final M2Model m2Model : modelsToRegister) {
			for (final M2Namespace m2Namespace : m2Model.getNamespaces()) {
				logger.debug("{} will provide namespace '{}'", m2Model.getName(), m2Namespace.getUri());
				namespaceProviders.put(m2Namespace.getUri(), m2Model);
			}
		}
		final Set<M2Model> registeredModels = new HashSet<M2Model>(modelsToRegister.size());
		for (final M2Model model : modelsToRegister) {
			final Set<M2Model> visitedModels = new HashSet<M2Model>(modelsToRegister.size());
			registerModel(model, namespaceProviders, registeredModels, visitedModels);
		}
	}

	public void unregisterModels() {
		for (final Iterator<QName> it = registeredModelNames.iterator(); it.hasNext();) {
			final QName modelName = it.next();
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Unregistering model: '{}'", modelName);
				}
				getDictionaryDao().removeModel(modelName);
			} finally {
				it.remove();
			}
		}
	}

	/* Utility operations */

	private void registerModel(final M2Model model, final Map<String, M2Model> namespaceProviders,
			final Set<M2Model> registeredModels, final Set<M2Model> visitedModels) {
		visitedModels.add(model);
		final List<M2Namespace> imports = model.getImports();
		for (final M2Namespace anImport : imports) {
			final M2Model providingModel = namespaceProviders.get(anImport.getUri());
			if (providingModel != null && !registeredModels.contains(providingModel)) {
				Assert.isTrue(
						!visitedModels.contains(providingModel),
						String.format("Circular dependency detected between %s and %s", model.getName(),
								providingModel.getName()));
				logger.debug("Discovered {} dependency on '{}', resolving {} first", new Object[] { model.getName(),
						anImport.getUri(), providingModel.getName() });
				registerModel(providingModel, namespaceProviders, registeredModels, visitedModels);
			}
		}
		if (!registeredModels.contains(model)) {
			try {
				if (logger.isDebugEnabled()) {
					logger.debug("Registering model '{}'", model.getName());
				}
				final QName modelName = getDictionaryDao().putModel(model);
				registeredModelNames.add(modelName);
				registeredModels.add(model);
			} catch (final DictionaryException e) {
				if (logger.isWarnEnabled()) {
					logger.warn(String.format("Could not register model '%s'", model.getName()), e);
				}
			}
		}
	}

	/* Dependencies */

	@Required
	public void setDictionaryDao(final DictionaryDAO dictionaryDao) {
		this.dictionaryDao = dictionaryDao;
	}

	protected DictionaryDAO getDictionaryDao() {
		return dictionaryDao;
	}

	/* Configuration */

	public void setModels(final List<M2Model> models) {
		Assert.notNull(models, "Models cannot be null.");
		this.models = models;
	}

	public List<M2Model> getModels() {
		return models;
	}

	/* State */

	protected List<QName> getRegisteredModelNames() {
		return registeredModelNames;
	}

}
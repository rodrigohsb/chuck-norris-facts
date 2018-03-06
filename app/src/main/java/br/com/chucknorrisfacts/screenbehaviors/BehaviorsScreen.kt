package br.com.chucknorrisfacts.screenbehaviors

/**
 * @rodrigohsb
 */
interface BehaviorsScreen: EmptyState, GenericErrorView,
                            LoadingView, NoConnectionView,
                            TimeoutView, CleanableState
Rails.application.routes.draw do
  resources :replies
  resources :rooms do
    collection {post :import}
  end
  resources :reports
  get 'main/search'

  get 'main/report'

  get 'main/detail'

  get 'main/comments'
  post 'main/comments'

  get 'main/index'

  post 'main/search'

  root 'main#index'




  # For details on the DSL available within this file, see http://guides.rubyonrails.org/routing.html
end
